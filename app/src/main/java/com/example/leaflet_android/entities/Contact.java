package com.example.leaflet_android.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Contact {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String displayname;
    private String profilePic;
    private String lastMessage;


    public Contact(String content) {
        this.lastMessage = content;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return lastMessage;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.lastMessage = content;
    }

}
