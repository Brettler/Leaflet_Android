package com.example.leaflet_android.entities;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class ChatMessage {
    @PrimaryKey(autoGenerate = true)
    private int localID;

    private String id;
    private String created;
    @Embedded(prefix = "sender_")
    private Sender sender;
    private String content;

    public ChatMessage(String id, String created, Sender sender, String content) {
        this.id = id;
        this.created = created;
        this.sender = sender;
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

    public Sender getSender() {
        return sender;
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

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public void setContent(String content) {
        this.content = content;
    }


}

