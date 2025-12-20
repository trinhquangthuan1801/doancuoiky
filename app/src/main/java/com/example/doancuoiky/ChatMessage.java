package com.example.doancuoiky;

public class ChatMessage {
    private String senderName;
    private String lastMessage;
    private String timestamp;
    private int avatarResource;

    public ChatMessage(String senderName, String lastMessage, String timestamp, int avatarResource) {
        this.senderName = senderName;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.avatarResource = avatarResource;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getAvatarResource() {
        return avatarResource;
    }
}