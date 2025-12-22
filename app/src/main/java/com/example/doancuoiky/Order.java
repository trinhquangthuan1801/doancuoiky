package com.example.doancuoiky;

public class Order {
    private int orderId;
    private int productId;
    private String buyerUsername;
    private String sellerUsername;
    private String purchasePrice;
    private String orderDate;
    private String shippingAddress;
    private String contactPhone;

    // Constructor
    public Order(int orderId, int productId, String buyerUsername, String sellerUsername, String purchasePrice, String orderDate, String shippingAddress, String contactPhone) {
        this.orderId = orderId;
        this.productId = productId;
        this.buyerUsername = buyerUsername;
        this.sellerUsername = sellerUsername;
        this.purchasePrice = purchasePrice;
        this.orderDate = orderDate;
        this.shippingAddress = shippingAddress;
        this.contactPhone = contactPhone;
    }

    // Getters
    public int getOrderId() {
        return orderId;
    }

    public int getProductId() {
        return productId;
    }

    public String getBuyerUsername() {
        return buyerUsername;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public String getPurchasePrice() {
        return purchasePrice;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public String getContactPhone() {
        return contactPhone;
    }
}
