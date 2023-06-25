package com.example.leaflet_android.chat;

public class ContentMessageBody {
    private String message;

    public ContentMessageBody(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
