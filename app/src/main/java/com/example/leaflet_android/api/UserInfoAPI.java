package com.example.leaflet_android.api;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.leaflet_android.LeafletApp;
import com.example.leaflet_android.R;
import com.example.leaflet_android.login.UserInfo;

import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserInfoAPI {
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;

    public UserInfoAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(LeafletApp.context.getString(R.string.BaseUrl))
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void fetchUserInfo(String token, String id, final MutableLiveData<UserInfo> userInfoData) {
        Call<UserInfo> call = webServiceAPI.getUserInfo("Bearer " + token, id);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.isSuccessful()) {
                    UserInfo userInfo = response.body();
                    Log.d("UserInfoAPI", "UserInfo: " + userInfo.toString()); // This line prints the UserInfo object
                    userInfoData.postValue(userInfo);
                } else {
                    userInfoData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                userInfoData.postValue(null);
            }
        });
    }
}

