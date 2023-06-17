package com.example.leaflet_android;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.leaflet_android.entities.Contact;

@Database(entities = {Contact.class}, version = 1)
public abstract class AppDB extends RoomDatabase {
    public abstract ContactDao contactDao();
}