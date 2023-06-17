package com.example.leaflet_android.api;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.leaflet_android.LeafletApp;
import com.example.leaflet_android.MainActivity;
import com.example.leaflet_android.R;
import com.example.leaflet_android.register.UserRegister;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterAPI {
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;

    public RegisterAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(LeafletApp.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void registerUser(UserRegister user, Callback<Void> callback) {
        Call<Void> call = webServiceAPI.createUser(user);
        call.enqueue(callback);
    }



}

