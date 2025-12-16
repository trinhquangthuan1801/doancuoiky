package com.example.doancuoiky;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ProductManager.db";
    private static final int DATABASE_VERSION = 4; // Tăng version lên 4
    private static final String TABLE_PRODUCTS = "products";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_IMAGE = "image_resource";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_OWNER = "owner"; // Cột mới

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PRICE + " TEXT, " +
                COLUMN_IMAGE + " INTEGER, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_STATUS + " TEXT, " +
                COLUMN_OWNER + " TEXT)"; // Thêm cột owner
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    // --- CÁC HÀM XỬ LÝ DỮ LIỆU ---

    public void addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, product.getName());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_IMAGE, product.getImageResource());
        values.put(COLUMN_CATEGORY, product.getCategory());
        values.put(COLUMN_STATUS, product.getStatus());
        values.put(COLUMN_OWNER, product.getOwner()); // Lưu owner

        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }

    // Lấy sản phẩm của một người dùng cụ thể
    public List<Product> getProductsByOwner(String ownerName) {
        List<Product> productList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_OWNER + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{ownerName});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                String price = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRICE));
                int imageRes = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS));
                String owner = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OWNER));

                productList.add(new Product(id, name, price, imageRes, category, status, owner));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productList;
    }

    // Lấy danh sách sản phẩm ĐÃ DUYỆT
    public List<Product> getAllApprovedProducts() {
        List<Product> productList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_STATUS + " = 'approved'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                String price = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRICE));
                int imageRes = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS));
                String owner = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OWNER));

                productList.add(new Product(id, name, price, imageRes, category, status, owner));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productList;
    }

    // Lấy sản phẩm CHỜ DUYỆT (Pending)
    public List<Product> getPendingProducts() {
        List<Product> productList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_STATUS + " = 'pending'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                String price = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRICE));
                int imageRes = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS));
                String owner = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OWNER));

                productList.add(new Product(id, name, price, imageRes, category, status, owner));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productList;
    }

    // Duyệt sản phẩm
    public void approveProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, "approved");
        
        db.update(TABLE_PRODUCTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(productId)});
        db.close();
    }

    // Xóa sản phẩm
    public void deleteProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, COLUMN_ID + " = ?", new String[]{String.valueOf(productId)});
        db.close();
    }

    // Lấy sản phẩm theo danh mục và ĐÃ DUYỆT
    public List<Product> getApprovedProductsByCategory(String categoryFilter) {
        List<Product> productList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_CATEGORY + " = ? AND " + COLUMN_STATUS + " = 'approved'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{categoryFilter});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                String price = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRICE));
                int imageRes = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS));
                String owner = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OWNER));

                productList.add(new Product(id, name, price, imageRes, category, status, owner));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productList;
    }

    public void createDefaultDataIfEmpty() {
        if (getAllApprovedProducts().isEmpty()) {
            addProduct(new Product("iPhone 14", "$999", R.drawable.ic_launcher_background, "Electronics", "approved", "admin"));
            addProduct(new Product("Áo thun", "$20", R.drawable.ic_launcher_background, "Personal Items", "approved", "admin"));
            addProduct(new Product("Sách Java", "$30", R.drawable.ic_launcher_background, "Books", "approved", "admin"));
            addProduct(new Product("MacBook Pro", "$2000", R.drawable.ic_launcher_background, "Electronics", "approved", "admin"));
            addProduct(new Product("Giày Nike", "$100", R.drawable.ic_launcher_background, "Personal Items", "approved", "admin"));
            addProduct(new Product("Truyện Harry Potter", "$15", R.drawable.ic_launcher_background, "Books", "approved", "admin"));
        }
    }
}