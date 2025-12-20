package com.example.doancuoiky;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class AppDatabaseHelper extends SQLiteOpenHelper {

    // Tên Database mới (Khác với cái cũ để tránh xung đột)
    private static final String DATABASE_NAME = "DoanCuoiKy.db";
    private static final int DATABASE_VERSION = 1;

    // --- TÊN CÁC BẢNG ---
    public static final String TABLE_USERS = "Users";
    public static final String TABLE_CATEGORIES = "Categories";
    public static final String TABLE_PRODUCTS = "Products";
    public static final String TABLE_PRODUCT_IMAGES = "ProductImages";
    public static final String TABLE_ORDERS = "Orders";
    public static final String TABLE_REVIEWS = "Reviews";
    public static final String TABLE_FAVORITES = "Favorites";

    public AppDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Bật tính năng khóa ngoại (Foreign Key)
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. BẢNG USERS
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

        // 2. BẢNG CATEGORIES
        String createCategories = "CREATE TABLE " + TABLE_CATEGORIES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL)";
        db.execSQL(createCategories);

        // 3. BẢNG PRODUCTS
        String createProducts = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "price REAL NOT NULL, " +
                "description TEXT, " +
                "condition_percentage INTEGER, " +
                "category_id INTEGER, " +
                "user_id INTEGER, " +
                "status INTEGER DEFAULT 0, " + // 0: Chờ duyệt, 1: Hiện, 2: Đã bán
                "created_at TEXT, " +
                "FOREIGN KEY(category_id) REFERENCES " + TABLE_CATEGORIES + "(id), " +
                "FOREIGN KEY(user_id) REFERENCES " + TABLE_USERS + "(id))";
        db.execSQL(createProducts);

        // 4. BẢNG PRODUCT_IMAGES
        String createImages = "CREATE TABLE " + TABLE_PRODUCT_IMAGES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "product_id INTEGER, " +
                "image_uri TEXT, " +
                "FOREIGN KEY(product_id) REFERENCES " + TABLE_PRODUCTS + "(id) ON DELETE CASCADE)";
        db.execSQL(createImages);

        // 5. BẢNG ORDERS
        String createOrders = "CREATE TABLE " + TABLE_ORDERS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "buyer_id INTEGER, " +
                "product_id INTEGER, " +
                "order_date TEXT, " +
                "total_price REAL, " +
                "shipping_address TEXT, " +
                "status INTEGER DEFAULT 0, " +
                "FOREIGN KEY(buyer_id) REFERENCES " + TABLE_USERS + "(id), " +
                "FOREIGN KEY(product_id) REFERENCES " + TABLE_PRODUCTS + "(id))";
        db.execSQL(createOrders);

        // 6. BẢNG REVIEWS
        String createReviews = "CREATE TABLE " + TABLE_REVIEWS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "order_id INTEGER, " +
                "rating INTEGER, " +
                "comment TEXT, " +
                "created_at TEXT, " +
                "FOREIGN KEY(order_id) REFERENCES " + TABLE_ORDERS + "(id))";
        db.execSQL(createReviews);

        // 7. BẢNG FAVORITES
        String createFavorites = "CREATE TABLE " + TABLE_FAVORITES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "product_id INTEGER, " +
                "created_at TEXT, " +
                "FOREIGN KEY(user_id) REFERENCES " + TABLE_USERS + "(id), " +
                "FOREIGN KEY(product_id) REFERENCES " + TABLE_PRODUCTS + "(id), " +
                "UNIQUE(user_id, product_id))";
        db.execSQL(createFavorites);

        // --- TẠO DỮ LIỆU MẪU (MẶC ĐỊNH) ---
        insertDefaultCategories(db);
        insertAdminUser(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REVIEWS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_IMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // --- CÁC HÀM HỖ TRỢ KHỞI TẠO ---
    private void insertDefaultCategories(SQLiteDatabase db) {
        String[] categories = {"Điện tử", "Thời trang", "Sách", "Nội thất", "Xe cộ", "Khác"};
        for (String cat : categories) {
            ContentValues values = new ContentValues();
            values.put("name", cat);
            db.insert(TABLE_CATEGORIES, null, values);
        }
    }

    private void insertAdminUser(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("email", "admin@gmail.com");
        values.put("password", "123456"); // Lưu ý: Thực tế nên mã hóa MD5/SHA
        values.put("full_name", "Administrator");
        values.put("role", "ADMIN");
        values.put("created_at", String.valueOf(System.currentTimeMillis()));
        db.insert(TABLE_USERS, null, values);
    }

    // ===============================================================
    // DƯỚI ĐÂY LÀ CÁC HÀM MẪU CRUD (THÊM, XEM, SỬA, XÓA)
    // Tương thích với cấu trúc bảng mới của bạn
    // ===============================================================

    // 1. Đăng ký User mới
    public long registerUser(String email, String password, String fullName, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("password", password);
        values.put("full_name", fullName);
        values.put("phone", phone);
        values.put("role", "USER");
        values.put("created_at", String.valueOf(System.currentTimeMillis()));
        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }

    // 2. Check Login
    public boolean checkLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE email=? AND password=?", new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // 3. Thêm sản phẩm mới (Lưu ý: chưa lưu ảnh ở hàm này, ảnh lưu bảng riêng)
    public long addProduct(String name, double price, String desc, int condition, int categoryId, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("description", desc);
        values.put("condition_percentage", condition);
        values.put("category_id", categoryId);
        values.put("user_id", userId);
        values.put("status", 0); // Mặc định là 0: Chờ duyệt
        values.put("created_at", String.valueOf(System.currentTimeMillis()));

        long productId = db.insert(TABLE_PRODUCTS, null, values);
        db.close();
        return productId;
    }

    // 4. Thêm ảnh cho sản phẩm
    public void addProductImage(long productId, String imageUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("product_id", productId);
        values.put("image_uri", imageUri);
        db.insert(TABLE_PRODUCT_IMAGES, null, values);
        db.close();
    }

    // 5. Lấy danh sách sản phẩm ĐÃ DUYỆT (status = 1) để hiển thị trang chủ
    public Cursor getAllActiveProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        // Query nối bảng để lấy tên danh mục và tên người bán nếu cần
        String query = "SELECT p.*, c.name as category_name, u.full_name as seller_name " +
                "FROM " + TABLE_PRODUCTS + " p " +
                "LEFT JOIN " + TABLE_CATEGORIES + " c ON p.category_id = c.id " +
                "LEFT JOIN " + TABLE_USERS + " u ON p.user_id = u.id " +
                "WHERE p.status = 1 " +
                "ORDER BY p.id DESC";
        return db.rawQuery(query, null);
    }

    // 6. Lấy danh sách ảnh của 1 sản phẩm
    public List<String> getProductImages(int productId) {
        List<String> images = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT image_uri FROM " + TABLE_PRODUCT_IMAGES + " WHERE product_id=?",
                new String[]{String.valueOf(productId)});
        if (cursor.moveToFirst()) {
            do {
                images.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return images;
    }

    // 7. Admin duyệt sản phẩm (Đổi status từ 0 -> 1)
    public void approveProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", 1); // 1: Approved
        db.update(TABLE_PRODUCTS, values, "id=?", new String[]{String.valueOf(productId)});
        db.close();
    }
}