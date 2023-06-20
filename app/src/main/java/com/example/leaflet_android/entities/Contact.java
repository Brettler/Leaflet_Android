package com.example.leaflet_android.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Contact {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String displayName;
    private int profilePic;
    private String lastMessage;

    public Contact(String displayName, int profilePic, String lastMessage) {
        this.displayName = displayName;
        this.profilePic = profilePic;
        this.lastMessage = lastMessage;
    }

    // Setters
    public void setId(int id) {this.id = id;}

    public void setDisplayName(String displayName) {this.displayName = displayName;}

    public void setProfilePic(int profilePic) {this.profilePic = profilePic;}

    public void setLastMessage(String lastMessage) {this.lastMessage = lastMessage;}

    //Getters
    public String getLastMessage() {
        return lastMessage;
    }
    public String getDisplayName() {
        return displayName;
    }
    public int getProfilePic() {return profilePic;}

    public int getId() {
        return id;
    }
}
