package com.example.leaflet_android.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.leaflet_android.login.UserInfo;

@Dao
public interface UserInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserInfo userInfo);

    @Query("SELECT * FROM userinfo WHERE username = :username")
    UserInfo getUserInfo(String username);
}

