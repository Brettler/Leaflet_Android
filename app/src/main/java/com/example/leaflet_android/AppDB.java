package com.example.leaflet_android;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.leaflet_android.dao.ChatMessageDao;
import com.example.leaflet_android.dao.ContactDao;
import com.example.leaflet_android.entities.ChatMessage;
import com.example.leaflet_android.entities.Contact;
import com.example.leaflet_android.login.UserInfo;
import com.example.leaflet_android.dao.UserInfoDao;

@Database(entities = {Contact.class, UserInfo.class, ChatMessage.class}, version = 13)
public abstract class AppDB extends RoomDatabase {
    public abstract ContactDao contactDao();
    public abstract UserInfoDao userInfoDao();
    public abstract ChatMessageDao chatMessageDao();
    private static volatile AppDB INSTANCE;

    public static AppDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDB.class, "LeafletDB")
                            .fallbackToDestructiveMigration()  // Added this line
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
