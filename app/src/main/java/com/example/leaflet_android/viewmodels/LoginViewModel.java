package com.example.leaflet_android.viewmodels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.leaflet_android.api.LoginAPI;
import com.example.leaflet_android.login.UserLogin;

// This ViewModel exposes the LiveData of token to the UI and has a method to initiate login.
public class LoginViewModel extends ViewModel {

    private MutableLiveData<String> loginToken;
    private LoginAPI loginAPI;

    public LoginViewModel() {
        loginAPI = new LoginAPI();
        loginToken = new MutableLiveData<>();
    }

    public LiveData<String> getLoginToken() {
        return loginToken;
    }

    public void loginUser(UserLogin user) {
        loginAPI.loginUser(user, loginToken);
    }
}


