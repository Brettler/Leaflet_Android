package com.example.leaflet_android.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class ChatMessage {
    @PrimaryKey(autoGenerate = true)
    private int localID;

    private String id;
    private String created;
    private String senderUsername;
    private String content;

    public ChatMessage(String id, String created, String senderUsername, String content) {
        this.id = id;
        this.created = created;
        this.senderUsername = senderUsername;
        this.content = content;
    }

    public int getLocalID() {
        return localID;
    }

    public void setLocalID(int localID) {
        this.localID = localID;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getCreated() {
        return created;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public String getContent() {
        return content;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public void setContent(String content) {
        this.content = content;
    }


}

