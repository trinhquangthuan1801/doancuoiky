package com.example.doancuoiky;

public class Product {
    private int id;
    private String name;
    private String price;
    private int imageResource;
    private String category;
    private String status;
    private String owner;
    private int quantity; // Thêm số lượng

    // Constructor đầy đủ khi đọc từ DB
    public Product(int id, String name, String price, int imageResource, String category, String status, String owner, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageResource = imageResource;
        this.category = category;
        this.status = status;
        this.owner = owner;
        this.quantity = quantity;
    }

    // Constructor khi tạo mới (chưa có ID)
    public Product(String name, String price, int imageResource, String category, String status, String owner, int quantity) {
        this.name = name;
        this.price = price;
        this.imageResource = imageResource;
        this.category = category;
        this.status = status;
        this.owner = owner;
        this.quantity = quantity;
    }

    // Constructor cũ (giữ lại để tương thích code cũ, mặc định quantity = 10)
    public Product(String name, String price, int imageResource, String category) {
        this(name, price, imageResource, category, "approved", "admin", 10);
    }
    
    // Constructor cũ hơn
    public Product(String name, String price, int imageResource) {
        this(name, price, imageResource, "Electronics", "approved", "admin", 10);
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public String getPrice() { return price; }
    public int getImageResource() { return imageResource; }
    public String getCategory() { return category; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}