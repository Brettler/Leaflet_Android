package com.example.leaflet_android.chat;

import com.example.leaflet_android.login.UserInfo;

public class SendMessageObject {
    private String id;
    private String created;
    private UserInfo sender;
    private String content;

    public SendMessageObject(String id, String created, UserInfo sender, String content) {
        this.id = id;
        this.created = created;
        this.sender = sender;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public UserInfo getSender() {
        return sender;
    }

    public void setSender(UserInfo sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
