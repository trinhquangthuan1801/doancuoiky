package com.example.doancuoiky;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class AppDatabaseHelper extends SQLiteOpenHelper {

    // Tên Database
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
                "role TEXT DEFAULT 'USER', " + // ADMIN hoặc USER
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
                "user_id INTEGER, " + // Quan trọng: Ai là người bán
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

        // --- TẠO DỮ LIỆU MẪU ---
        insertDefaultCategories(db);
        insertDefaultUsers(db);    // <-- Đã thêm: Tạo Admin, User1, User2
        insertDefaultProducts(db); // <-- Đã thêm: Tạo sản phẩm mẫu cho User1, User2
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

    // --- CÁC HÀM HỖ TRỢ KHỞI TẠO DỮ LIỆU ---

    private void insertDefaultCategories(SQLiteDatabase db) {
        String[] categories = {"Điện tử", "Thời trang", "Sách", "Nội thất", "Xe cộ", "Khác"};
        for (String cat : categories) {
            ContentValues values = new ContentValues();
            values.put("name", cat);
            db.insert(TABLE_CATEGORIES, null, values);
        }
    }

    private void insertDefaultUsers(SQLiteDatabase db) {
        // 1. ADMIN (ID = 1)
        ContentValues admin = new ContentValues();
        admin.put("email", "admin@gmail.com");
        admin.put("password", "123");
        admin.put("full_name", "Administrator");
        admin.put("role", "ADMIN");
        admin.put("created_at", String.valueOf(System.currentTimeMillis()));
        db.insert(TABLE_USERS, null, admin);

        // 2. USER 1 (ID = 2) - Người bán iPhone, Xe đạp
        ContentValues user1 = new ContentValues();
        user1.put("email", "user1@gmail.com");
        user1.put("password", "123");
        user1.put("full_name", "Nguyen Van A");
        user1.put("role", "USER");
        user1.put("created_at", String.valueOf(System.currentTimeMillis()));
        db.insert(TABLE_USERS, null, user1);

        // 3. USER 2 (ID = 3) - Người bán Áo khoác
        ContentValues user2 = new ContentValues();
        user2.put("email", "user2@gmail.com");
        user2.put("password", "123");
        user2.put("full_name", "Tran Thi B");
        user2.put("role", "USER");
        user2.put("created_at", String.valueOf(System.currentTimeMillis()));
        db.insert(TABLE_USERS, null, user2);
    }

    private void insertDefaultProducts(SQLiteDatabase db) {
        String sql = "INSERT INTO " + TABLE_PRODUCTS +
                " (name, price, description, condition_percentage, category_id, user_id, status, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        long time = System.currentTimeMillis();

        // Sản phẩm của USER 1 (ID=2)
        // - iPhone 14 (Đã duyệt)
        db.execSQL(sql, new Object[]{"iPhone 14 Pro Max", 20000000.0, "Máy còn mới 98%, pin trâu", 98, 1, 2, 1, time});
        // - Xe đạp (Chờ duyệt - để Admin test chức năng duyệt)
        db.execSQL(sql, new Object[]{"Xe đạp Martin cũ", 1500000.0, "Xe học sinh, hơi trầy xước", 80, 5, 2, 0, time});

        // Sản phẩm của USER 2 (ID=3)
        // - Áo khoác (Đã duyệt)
        db.execSQL(sql, new Object[]{"Áo khoác da", 500000.0, "Hàng xách tay Mỹ", 100, 2, 3, 1, time});
    }

    // ===============================================================
    // CÁC HÀM CRUD CƠ BẢN
    // ===============================================================

    // 1. Đăng ký
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

    // 2. Đăng nhập (Sửa lại: Trả về Cursor chứa thông tin User thay vì boolean)
    // Lý do: Để lấy được ID, Role, FullName sau khi đăng nhập thành công
    public Cursor getUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE email=? AND password=?", new String[]{email, password});
    }

    // 3. Thêm sản phẩm (Dành cho chức năng "Đăng bán")
    public long addProduct(String name, double price, String desc, int condition, int categoryId, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("description", desc);
        values.put("condition_percentage", condition);
        values.put("category_id", categoryId);
        values.put("user_id", userId);
        values.put("status", 0); // Mặc định là Chờ duyệt (0)
        values.put("created_at", String.valueOf(System.currentTimeMillis()));

        long productId = db.insert(TABLE_PRODUCTS, null, values);
        db.close();
        return productId;
    }

    // 4. Lấy danh sách sản phẩm hiển thị trang chủ (Chỉ lấy status = 1)
    // Có join bảng User để lấy tên người bán
    public Cursor getAllActiveProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT p.*, c.name as category_name, u.full_name as seller_name " +
                "FROM " + TABLE_PRODUCTS + " p " +
                "JOIN " + TABLE_CATEGORIES + " c ON p.category_id = c.id " +
                "JOIN " + TABLE_USERS + " u ON p.user_id = u.id " +
                "WHERE p.status = 1 " +
                "ORDER BY p.id DESC";
        return db.rawQuery(query, null);
    }

    // 5. Admin: Lấy danh sách sản phẩm CHỜ DUYỆT (status = 0)
    public Cursor getPendingProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT p.*, u.full_name as seller_name " +
                "FROM " + TABLE_PRODUCTS + " p " +
                "JOIN " + TABLE_USERS + " u ON p.user_id = u.id " +
                "WHERE p.status = 0 " +
                "ORDER BY p.created_at ASC";
        return db.rawQuery(query, null);
    }

    // 6. Admin: Duyệt sản phẩm
    public void approveProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", 1); // 1: Đã duyệt
        db.update(TABLE_PRODUCTS, values, "id=?", new String[]{String.valueOf(productId)});
        db.close();
    }
}