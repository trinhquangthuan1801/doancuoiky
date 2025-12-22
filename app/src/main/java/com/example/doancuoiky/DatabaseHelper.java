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
    private static final int DATABASE_VERSION = 8;

    // === Tên bảng và cột ===
    private static final String TABLE_PRODUCTS = "products";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_IMAGE = "image_path";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_OWNER = "owner";
    private static final String COLUMN_DESCRIPTION = "description";

    private static final String TABLE_CART = "cart_items";
    private static final String COLUMN_CART_ID = "cart_item_id";
    private static final String COLUMN_CART_USER_NAME = "user_name";
    private static final String COLUMN_CART_PRODUCT_ID = "product_id";
    private static final String COLUMN_CART_QUANTITY = "quantity";

    private static final String TABLE_ORDERS = "orders";
    private static final String COLUMN_ORDER_ID = "order_id";
    private static final String COLUMN_ORDER_PRODUCT_ID = "product_id";
    private static final String COLUMN_ORDER_BUYER_USERNAME = "buyer_username";
    private static final String COLUMN_ORDER_SELLER_USERNAME = "seller_username";
    private static final String COLUMN_ORDER_PURCHASE_PRICE = "purchase_price";
    private static final String COLUMN_ORDER_DATE = "order_date";
    private static final String COLUMN_ORDER_SHIPPING_ADDRESS = "shipping_address";
    private static final String COLUMN_ORDER_CONTACT_PHONE = "contact_phone";

    public static class CartItem {
        private int id;
        private Product product;
        private int quantity;

        public CartItem(int id, Product product, int quantity) {
            this.id = id;
            this.product = product;
            this.quantity = quantity;
        }

        public int getId() { return id; }
        public Product getProduct() { return product; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createProductsTable = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT, " +
                COLUMN_PRICE + " TEXT, " + COLUMN_IMAGE + " TEXT, " + COLUMN_CATEGORY + " TEXT, " +
                COLUMN_STATUS + " TEXT, " + COLUMN_OWNER + " TEXT, " + COLUMN_DESCRIPTION + " TEXT)";
        db.execSQL(createProductsTable);

        String createCartTable = "CREATE TABLE " + TABLE_CART + " (" +
                COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_CART_USER_NAME + " TEXT, " +
                COLUMN_CART_PRODUCT_ID + " INTEGER, " + COLUMN_CART_QUANTITY + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_CART_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + COLUMN_ID + "))";
        db.execSQL(createCartTable);

        String createOrdersTable = "CREATE TABLE " + TABLE_ORDERS + " (" +
                COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_ORDER_PRODUCT_ID + " INTEGER, " +
                COLUMN_ORDER_BUYER_USERNAME + " TEXT, " + COLUMN_ORDER_SELLER_USERNAME + " TEXT, " +
                COLUMN_ORDER_PURCHASE_PRICE + " TEXT, " + COLUMN_ORDER_DATE + " TEXT, " +
                COLUMN_ORDER_SHIPPING_ADDRESS + " TEXT, " + COLUMN_ORDER_CONTACT_PHONE + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_ORDER_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + COLUMN_ID + "))";
        db.execSQL(createOrdersTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db);
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

    private Order extractOrder(Cursor cursor) {
        int orderId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID));
        int productId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_PRODUCT_ID));
        String buyerUsername = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_BUYER_USERNAME));
        String sellerUsername = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_SELLER_USERNAME));
        String purchasePrice = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_PURCHASE_PRICE));
        String orderDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_DATE));
        String address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_SHIPPING_ADDRESS));
        String phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_CONTACT_PHONE));
        return new Order(orderId, productId, buyerUsername, sellerUsername, purchasePrice, orderDate, address, phone);
    }

    // === CÁC HÀM CRUD (ĐÃ XÓA TẤT CẢ db.close()) ===

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
    }

    public List<Product> getProductsByOwner(String ownerName) {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_OWNER + " = ?", new String[]{ownerName});
        if (cursor.moveToFirst()) {
            do { productList.add(extractProduct(cursor)); } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    public List<Product> getAllApprovedProducts() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_STATUS + " = ?", new String[]{"approved"});
        if (cursor.moveToFirst()) {
            do { productList.add(extractProduct(cursor)); } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    public List<Product> getPendingProducts() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_STATUS + " = ?", new String[]{"pending"});
        if (cursor.moveToFirst()) {
            do { productList.add(extractProduct(cursor)); } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    public Product getProductById(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Product product = null;
        Cursor cursor = db.query(TABLE_PRODUCTS, null, COLUMN_ID + " = ?", new String[]{String.valueOf(productId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            product = extractProduct(cursor);
        }
        if (cursor != null) {
            cursor.close();
        }
        return product;
    }

    public void approveProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, "approved");
        db.update(TABLE_PRODUCTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(productId)});
    }

    public void deleteProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, COLUMN_ID + " = ?", new String[]{String.valueOf(productId)});
    }

    public void markProductAsSold(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, "sold");
        db.update(TABLE_PRODUCTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(productId)});
    }

    public void addOrder(int productId, String buyerUsername, String sellerUsername, String price, String date, String address, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_PRODUCT_ID, productId);
        values.put(COLUMN_ORDER_BUYER_USERNAME, buyerUsername);
        values.put(COLUMN_ORDER_SELLER_USERNAME, sellerUsername);
        values.put(COLUMN_ORDER_PURCHASE_PRICE, price);
        values.put(COLUMN_ORDER_DATE, date);
        values.put(COLUMN_ORDER_SHIPPING_ADDRESS, address);
        values.put(COLUMN_ORDER_CONTACT_PHONE, phone);
        db.insert(TABLE_ORDERS, null, values);
    }

    public List<Product> getApprovedProductsByCategory(String categoryFilter) {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_CATEGORY + " = ? AND " + COLUMN_STATUS + " = ?", new String[]{categoryFilter, "approved"});
        if (cursor.moveToFirst()) {
            do { productList.add(extractProduct(cursor)); } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    public List<Order> getPurchaseHistory(String username) {
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ORDERS, null, COLUMN_ORDER_BUYER_USERNAME + " = ?", new String[]{username}, null, null, COLUMN_ORDER_ID + " DESC");
        if (cursor.moveToFirst()) {
            do { orderList.add(extractOrder(cursor)); } while (cursor.moveToNext());
        }
        cursor.close();
        return orderList;
    }

    public List<Order> getSalesHistory(String username) {
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ORDERS, null, COLUMN_ORDER_SELLER_USERNAME + " = ?", new String[]{username}, null, null, COLUMN_ORDER_ID + " DESC");
        if (cursor.moveToFirst()) {
            do { orderList.add(extractOrder(cursor)); } while (cursor.moveToNext());
        }
        cursor.close();
        return orderList;
    }

    // === CART METHODS ===
    public void addToCart(int productId, String userName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART + " WHERE " + COLUMN_CART_PRODUCT_ID + " = ? AND " + COLUMN_CART_USER_NAME + " = ?", new String[]{String.valueOf(productId), userName});
        if (cursor.moveToFirst()) {
            int cartId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_ID));
            int currentQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_QUANTITY));
            ContentValues values = new ContentValues();
            values.put(COLUMN_CART_QUANTITY, currentQuantity + 1);
            db.update(TABLE_CART, values, COLUMN_CART_ID + " = ?", new String[]{String.valueOf(cartId)});
        } else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CART_PRODUCT_ID, productId);
            values.put(COLUMN_CART_USER_NAME, userName);
            values.put(COLUMN_CART_QUANTITY, 1);
            db.insert(TABLE_CART, null, values);
        }
        cursor.close();
    }

    public List<CartItem> getCartItems(String userName) {
        List<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT c." + COLUMN_CART_ID + ", c." + COLUMN_CART_QUANTITY + ", p.* FROM " + TABLE_CART + " AS c JOIN " + TABLE_PRODUCTS + " AS p ON c." + COLUMN_CART_PRODUCT_ID + " = p." + COLUMN_ID + " WHERE c." + COLUMN_CART_USER_NAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{userName});
        if (cursor.moveToFirst()) {
            do {
                Product product = extractProduct(cursor);
                int cartId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_ID));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_QUANTITY));
                cartItems.add(new CartItem(cartId, product, quantity));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cartItems;
    }

    public void updateCartItemQuantity(int cartItemId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CART_QUANTITY, quantity);
        db.update(TABLE_CART, values, COLUMN_CART_ID + " = ?", new String[]{String.valueOf(cartItemId)});
    }

    public void deleteCartItem(int cartItemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COLUMN_CART_ID + " = ?", new String[]{String.valueOf(cartItemId)});
    }
}
