package com.example.clammind;

public class ChatMessage {
    private String message;
    private boolean isUser;
    private boolean isTyping;

    public ChatMessage(String message, boolean isUser) {
        this.message = message;
        this.isUser = isUser;
        this.isTyping = false;
    }

    public ChatMessage(boolean isTyping) {
        this.isTyping = isTyping;
    }

    public String getMessage() {
        return message;
    }

    public boolean isUser() {
        return isUser;
    }

    public boolean isTyping() {
        return isTyping;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
