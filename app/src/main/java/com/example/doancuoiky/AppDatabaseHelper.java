package com.example.doancuoiky;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DoanCuoiKy.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_USERS = "Users";
    public static final String TABLE_CATEGORIES = "Categories";
    public static final String TABLE_PRODUCTS = "Products";
    public static final String TABLE_PRODUCT_IMAGES = "ProductImages";

    public AppDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsers = "CREATE TABLE " + TABLE_USERS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT UNIQUE NOT NULL, " +
                "password TEXT NOT NULL, " +
                "full_name TEXT, " +
                "phone TEXT, " +
                "avatar TEXT, " +
                "role TEXT DEFAULT 'USER', " +
                "created_at TEXT)";
        db.execSQL(createUsers);

        String createCategories = "CREATE TABLE " + TABLE_CATEGORIES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL)";
        db.execSQL(createCategories);

        String createProducts = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "price REAL NOT NULL, " +
                "description TEXT, " +
                "condition_percentage INTEGER, " +
                "category_id INTEGER, " +
                "user_id INTEGER, " +
                "status INTEGER DEFAULT 0, " +
                "created_at TEXT, " +
                "FOREIGN KEY(category_id) REFERENCES " + TABLE_CATEGORIES + "(id), " +
                "FOREIGN KEY(user_id) REFERENCES " + TABLE_USERS + "(id))";
        db.execSQL(createProducts);

        String createImages = "CREATE TABLE " + TABLE_PRODUCT_IMAGES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "product_id INTEGER, " +
                "image_uri TEXT, " +
                "FOREIGN KEY(product_id) REFERENCES " + TABLE_PRODUCTS + "(id) ON DELETE CASCADE)";
        db.execSQL(createImages);

        insertDefaultCategories(db);
        insertDefaultUsers(db);
        insertDefaultProducts(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_IMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    private void insertDefaultCategories(SQLiteDatabase db) {
        String[] categories = {"Điện tử", "Thời trang", "Sách", "Nội thất", "Xe cộ", "Khác"};
        for (String cat : categories) {
            ContentValues values = new ContentValues();
            values.put("name", cat);
            db.insert(TABLE_CATEGORIES, null, values);
        }
    }

    private void insertDefaultUsers(SQLiteDatabase db) {
        ContentValues admin = new ContentValues();
        admin.put("email", "admin@gmail.com");
        admin.put("password", "123");
        admin.put("full_name", "Administrator");
        admin.put("role", "admin");
        db.insert(TABLE_USERS, null, admin);

        ContentValues user1 = new ContentValues();
        user1.put("email", "user@gmail.com");
        user1.put("password", "123");
        user1.put("full_name", "Nguyen Van User");
        user1.put("role", "user");
        db.insert(TABLE_USERS, null, user1);
    }

    private void insertDefaultProducts(SQLiteDatabase db) {
        addProduct(db, "iPhone 14 Pro Max", 20000000.0, "Máy còn mới 98%, pin trâu", 98, 1, 2, 1);
        addProduct(db, "Xe đạp Martin cũ", 1500000.0, "Xe học sinh, hơi trầy xước", 80, 5, 2, 0);
        addProduct(db, "Áo khoác da", 500000.0, "Hàng xách tay Mỹ", 100, 2, 2, 1);
    }

    private void addProduct(SQLiteDatabase db, String name, double price, String desc, int condition, int catId, int userId, int status) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("description", desc);
        values.put("condition_percentage", condition);
        values.put("category_id", catId);
        values.put("user_id", userId);
        values.put("status", status);
        db.insert(TABLE_PRODUCTS, null, values);
    }

    // ===============================================================
    // CÁC HÀM CRUD (ĐÃ SỬA LỖI VÀ TỐI ƯU)
    // ===============================================================

    public Cursor getUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE email=? AND password=?", new String[]{email, password});
    }

    public String getUserEmailById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT email FROM " + TABLE_USERS + " WHERE id=?", new String[]{String.valueOf(userId)})) {
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow("email"));
            }
        }
        return "";
    }

    public long addProduct(String name, double price, String desc, int condition, int categoryId, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("description", desc);
        values.put("condition_percentage", condition);
        values.put("category_id", categoryId);
        values.put("user_id", userId);
        values.put("status", 0);
        return db.insert(TABLE_PRODUCTS, null, values);
    }

    public void addImageForProduct(long productId, String imageUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("product_id", productId);
        values.put("image_uri", imageUri);
        db.insert(TABLE_PRODUCT_IMAGES, null, values);
    }

    public Cursor getAllCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_CATEGORIES, null);
    }

    public Cursor getAllActiveProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT p.id, p.name, p.price, u.full_name as seller_name, MIN(pi.image_uri) as image_uri " +
                "FROM " + TABLE_PRODUCTS + " p " +
                "JOIN " + TABLE_USERS + " u ON p.user_id = u.id " +
                "LEFT JOIN " + TABLE_PRODUCT_IMAGES + " pi ON p.id = pi.product_id " +
                "WHERE p.status = 1 " +
                "GROUP BY p.id " +
                "ORDER BY p.id DESC";
        return db.rawQuery(query, null);
    }

    public Cursor getProductsByOwner(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT p.*, MIN(pi.image_uri) as image_uri " +
                       "FROM " + TABLE_PRODUCTS + " p " +
                       "LEFT JOIN " + TABLE_PRODUCT_IMAGES + " pi ON p.id = pi.product_id " +
                       "WHERE p.user_id = ? " +
                       "GROUP BY p.id " +
                       "ORDER BY p.id DESC";
        return db.rawQuery(query, new String[]{String.valueOf(userId)});
    }

    public Cursor getPendingProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT p.id, p.name, p.price, p.condition_percentage, u.full_name as seller_name, MIN(pi.image_uri) as image_uri " +
                "FROM " + TABLE_PRODUCTS + " p " +
                "JOIN " + TABLE_USERS + " u ON p.user_id = u.id " +
                "LEFT JOIN " + TABLE_PRODUCT_IMAGES + " pi ON p.id = pi.product_id " +
                "WHERE p.status = 0 " +
                "GROUP BY p.id " +
                "ORDER BY p.created_at ASC";
        return db.rawQuery(query, null);
    }

    public void updateProductStatus(long productId, int newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", newStatus);
        db.update(TABLE_PRODUCTS, values, "id = ?", new String[]{String.valueOf(productId)});
    }
}
