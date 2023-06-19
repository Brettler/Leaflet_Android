package com.example.leaflet_android;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.leaflet_android.entities.Contact;
import com.example.leaflet_android.login.UserInfo;
import com.example.leaflet_android.login.UserInfoDao;

@Database(entities = {Contact.class, UserInfo.class}, version = 3)
public abstract class AppDB extends RoomDatabase {
    public abstract ContactDao contactDao();
    public abstract UserInfoDao userInfoDao();

    private static volatile AppDB INSTANCE;

    public static AppDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDB.class, "app_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
