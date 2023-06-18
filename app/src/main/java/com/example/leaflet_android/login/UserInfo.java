package com.example.leaflet_android.login;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserInfo {
    @PrimaryKey
    @NonNull
    private String username;
    private String displayName;
    private int profilePic;

    // Constructor, getters and setters

    public UserInfo(@NonNull String username, String displayName, int profilePic) {
        this.username = username;
        this.displayName = displayName;
        this.profilePic = profilePic;
    }


    @NonNull
    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getProfilePic() {
        return profilePic;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setProfilePic(int profilePic) {
        this.profilePic = profilePic;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "username='" + username + '\'' +
                ", displayName='" + displayName + '\'' +
                ", profilePic='" + profilePic + '\'' +
                '}';
    }
}

