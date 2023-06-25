package com.example.leaflet_android.entities;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = "id", unique = true)})
public class Contact {
    @PrimaryKey(autoGenerate = true)
    private int localID;
    private String id;
    @Embedded(prefix = "user_")
    private UserContact user;
    @Embedded(prefix = "last_message_")
    private LastMessage lastMessage;

    public Contact(String id, UserContact user, LastMessage lastMessage) {
        this.id = id;
        this.user = user;
        this.lastMessage = lastMessage;
    }
    // Setters
    public void setLocalID(int localID) {
        this.localID = localID;
    }

    public void setID(String id) {
        this.id = id;
    }

    public void setUser(UserContact user) {
        this.user = user;
    }

    public void setLastMessage(LastMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    // Getters
    public int getLocalID() {
        return localID;
    }

    public String getId() {
        return id;
    }

    public UserContact getUser() {
        return user;
    }

    public LastMessage getLastMessage() {
        return lastMessage;
    }
}
