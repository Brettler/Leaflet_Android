package com.example.leaflet_android.api;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.leaflet_android.LeafletApp;
import com.example.leaflet_android.MainActivity;
import com.example.leaflet_android.R;
import com.example.leaflet_android.register.UserRegister;
import com.example.leaflet_android.settings.AppSettings;

import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterAPI {
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;

    private AppSettings appSettings;

    public RegisterAPI() {
        appSettings = new AppSettings(LeafletApp.context);
    }

    private Retrofit createRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(appSettings.getServerIpAddress())
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void registerUser(UserRegister user, Callback<Void> callback) {
        Retrofit retrofit = createRetrofitInstance();
        WebServiceAPI webServiceAPI = retrofit.create(WebServiceAPI.class);
        Call<Void> call = webServiceAPI.createUser(user);
        call.enqueue(callback);
    }

}

