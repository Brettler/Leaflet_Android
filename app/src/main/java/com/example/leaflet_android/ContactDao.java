package com.example.leaflet_android;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.leaflet_android.entities.Contact;

import java.util.List;

@Dao
public interface ContactDao {

    @Query("SELECT * FROM contact")
    List<Contact> index();

    @Query("SELECT * FROM contact WHERE id = :id")

    Contact get(int id);
    // recive list of contacts;
    @Insert
    void insert(Contact... contacts);

    @Delete
    void delete(Contact... contacts);

}
