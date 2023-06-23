package com.example.leaflet_android.api;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.leaflet_android.LeafletApp;
import com.example.leaflet_android.R;
import com.example.leaflet_android.login.UserInfo;
import com.example.leaflet_android.settings.AppSettings;

import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserInfoAPI {
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;

    private AppSettings appSettings;

    public UserInfoAPI() {
        appSettings = new AppSettings(LeafletApp.context);
    }

    private Retrofit createRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(appSettings.getServerIpAddress())
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void fetchUserInfo(String token, String firebaseToken, String id, final MutableLiveData<UserInfo> userInfoData) {
        Retrofit retrofit = createRetrofitInstance();
        WebServiceAPI webServiceAPI = retrofit.create(WebServiceAPI.class);
        Call<UserInfo> call = webServiceAPI.getUserInfo("Bearer " + token, firebaseToken, id);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.isSuccessful()) {
                    UserInfo userInfo = response.body();
                    userInfoData.postValue(userInfo);
                    Log.d("UserInfoAPI", "Response: " + userInfo); // Log the response
                } else {
                    userInfoData.postValue(null);
                    Log.e("UserInfoAPI", "Unsuccessful response: " + response.code()); // Log the error code
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                userInfoData.postValue(null);
                Log.e("UserInfoAPI", "Request failed: " + t.getMessage()); // Log the failure reason
            }
        });
    }
}

