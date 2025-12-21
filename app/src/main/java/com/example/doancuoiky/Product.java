package com.example.doancuoiky;

public class Product {
    private int id;
    private String name;
    private String price;
    private String imagePath; // Đã đổi từ int imageResource
    private String category;
    private String status;
    private String owner;
    private String description; // Đã thêm

    // Constructor đầy đủ khi đọc từ DB
    public Product(int id, String name, String price, String imagePath, String category, String status, String owner, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
        this.category = category;
        this.status = status;
        this.owner = owner;
        this.description = description;
    }

    // Constructor khi tạo mới (chưa có ID)
    public Product(String name, String price, String imagePath, String category, String status, String owner, String description) {
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
        this.category = category;
        this.status = status;
        this.owner = owner;
        this.description = description;
    }

    // --- Getters and Setters ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
