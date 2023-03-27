package com.example.messenger;

public class Message {
    private String senderId;
    private String recieverId;
    private String message;

    public Message() {
    }

    public Message(String senderId, String recieverId, String message) {
        this.senderId = senderId;
        this.recieverId = recieverId;
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getRecieverId() {
        return recieverId;
    }

    public String getMessage() {
        return message;
    }
}
