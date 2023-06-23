package com.example.leaflet_android.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.leaflet_android.entities.ChatMessage;

import java.util.List;

@Dao
public interface ChatMessageDao {

    @Query("SELECT * FROM ChatMessage")
    List<ChatMessage> index();

    @Query("SELECT * FROM ChatMessage WHERE localID = :localID")
    ChatMessage get(int localID);

    @Insert
    void insert(ChatMessage... messages);

    @Delete
    void delete(ChatMessage... messages);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ChatMessage> messages);
}