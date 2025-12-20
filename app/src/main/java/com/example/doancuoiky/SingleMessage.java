package com.example.doancuoiky;

public class SingleMessage {
    public static final int TYPE_SENT = 0;
    public static final int TYPE_RECEIVED = 1;

    private String content;
    private int type;

    public SingleMessage(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }
}