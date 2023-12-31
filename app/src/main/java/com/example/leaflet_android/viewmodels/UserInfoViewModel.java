package com.example.leaflet_android.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.leaflet_android.AppDB;
import com.example.leaflet_android.LeafletApp;
import com.example.leaflet_android.api.UserInfoAPI;
import com.example.leaflet_android.dao.UserInfoDao;
import com.example.leaflet_android.login.UserInfo;

public class UserInfoViewModel extends ViewModel {

//    private ImageView profileImageView;
//    private TextView displayNameTextView;
//    private TextView userNameTextView;
//
//    private UserInfoViewModel userInfoViewModel;

    private MutableLiveData<UserInfo> userInfoData;
    private UserInfoAPI userInfoAPI;
    private UserInfoDao userInfoDao;

    public UserInfoViewModel() {
        userInfoAPI = new UserInfoAPI();
        userInfoData = new MutableLiveData<>();
        AppDB db = AppDB.getDatabase(LeafletApp.context);
        userInfoDao = db.userInfoDao();
    }

    public LiveData<UserInfo> getUserInfo() {
        return userInfoData;
    }

    public void fetchUserInfo(String token, String firebaseToken, String id) {
        userInfoAPI.fetchUserInfo(token, firebaseToken, id, userInfoData);
    }


    public void storeUserInfo(UserInfo userInfo) {
        new Thread(() -> userInfoDao.insert(userInfo)).start();
    }

    public LiveData<UserInfo> getUserInfoFromDB(String username) {
        // LiveData from Room will automatically notify observer when data changes
        return userInfoDao.getUserInfo(username);
    }
}
