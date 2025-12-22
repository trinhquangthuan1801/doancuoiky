package com.example.doancuoiky;

public class Message {
    private String text;
    private String senderId;
    private long timestamp;
    private boolean isSentByCurrentUser;

    public Message() {
    }

    // SỬA LẠI CONSTRUCTOR Ở ĐÂY
    public Message(String text, boolean isSentByCurrentUser) {
        this.text = text;
        this.senderId = ""; // Tạm thời để trống
        this.isSentByCurrentUser = isSentByCurrentUser;
        this.timestamp = System.currentTimeMillis();
    }

    public String getText() {
        return text;
    }

    public String getSenderId() {
        return senderId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isSentByCurrentUser() {
        return isSentByCurrentUser;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setSentByCurrentUser(boolean sentByCurrentUser) {
        isSentByCurrentUser = sentByCurrentUser;
    }
}
