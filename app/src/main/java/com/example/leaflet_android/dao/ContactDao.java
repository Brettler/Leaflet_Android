package com.example.leaflet_android.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.leaflet_android.entities.Contact;

import java.util.List;

@Dao
public interface ContactDao {

    @Query("SELECT * FROM contact")
    List<Contact> index();

    @Query("SELECT * FROM contact WHERE localID = :localID")
    Contact get(int localID);

    // receive list of contacts;
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Contact... contacts);

    @Delete
    void delete(Contact... contacts);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Contact> contacts);

}
