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
    private static final int DATABASE_VERSION = 6; // Đặt lại phiên bản thành 6

    // === Bảng Products ===
    private static final String TABLE_PRODUCTS = "products";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_IMAGE = "image_path";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_OWNER = "owner";
    private static final String COLUMN_DESCRIPTION = "description";

    // === Bảng Cart (Giỏ hàng) ===
    private static final String TABLE_CART = "cart_items";
    private static final String COLUMN_CART_ID = "cart_item_id";
    private static final String COLUMN_CART_USER_NAME = "user_name";
    private static final String COLUMN_CART_PRODUCT_ID = "product_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createProductsTable = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PRICE + " TEXT, " +
                COLUMN_IMAGE + " TEXT, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_STATUS + " TEXT, " +
                COLUMN_OWNER + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT)";
        db.execSQL(createProductsTable);

        String createCartTable = "CREATE TABLE " + TABLE_CART + " (" +
                COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CART_USER_NAME + " TEXT, " +
                COLUMN_CART_PRODUCT_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_CART_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + COLUMN_ID + "))";
        db.execSQL(createCartTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    public void addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, product.getName());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_IMAGE, product.getImagePath());
        values.put(COLUMN_CATEGORY, product.getCategory());
        values.put(COLUMN_STATUS, product.getStatus());
        values.put(COLUMN_OWNER, product.getOwner());
        values.put(COLUMN_DESCRIPTION, product.getDescription());
        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }

    private Product extractProduct(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
        String price = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRICE));
        String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));
        String category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY));
        String status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS));
        String owner = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OWNER));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
        return new Product(id, name, price, imagePath, category, status, owner, description);
    }

    public List<Product> getProductsByOwner(String ownerName) {
        List<Product> productList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_OWNER + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{ownerName});
        if (cursor.moveToFirst()) {
            do {
                productList.add(extractProduct(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productList;
    }

    public List<Product> getAllApprovedProducts() {
        List<Product> productList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_STATUS + " = 'approved'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                productList.add(extractProduct(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productList;
    }

    public List<Product> getPendingProducts() {
        List<Product> productList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_STATUS + " = 'pending'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                productList.add(extractProduct(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productList;
    }

    public void approveProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, "approved");
        db.update(TABLE_PRODUCTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(productId)});
        db.close();
    }

    public void deleteProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, COLUMN_ID + " = ?", new String[]{String.valueOf(productId)});
        db.close();
    }

    public List<Product> getApprovedProductsByCategory(String categoryFilter) {
        List<Product> productList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_CATEGORY + " = ? AND " + COLUMN_STATUS + " = 'approved'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{categoryFilter});
        if (cursor.moveToFirst()) {
            do {
                productList.add(extractProduct(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productList;
    }
}
