package com.example.leaflet_android.api;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.leaflet_android.LeafletApp;
import com.example.leaflet_android.R;
import com.example.leaflet_android.login.UserLogin;
import com.example.leaflet_android.settings.AppSettings;

import java.io.IOException;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginAPI {
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;

    private AppSettings appSettings;

    public LoginAPI() {
        appSettings = new AppSettings(LeafletApp.context);
    }

    private Retrofit createRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(appSettings.getServerIpAddress())
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public void loginUser(UserLogin user, final MutableLiveData<String> tokenData) {
        Retrofit retrofit = createRetrofitInstance();
        WebServiceAPI webServiceAPI = retrofit.create(WebServiceAPI.class);

        Call<ResponseBody> call = webServiceAPI.userLogin(user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // When the response is successful, post the received token.
                    try {
                        String token = response.body().string();
                        if (token != null && !token.isEmpty()) {
                            tokenData.postValue(token);
                        } else {
                            tokenData.postValue("error");
                        }
                        Log.d("LoginAPI", "Response: " + token); // Log the response
                    } catch (IOException e) {
                        e.printStackTrace();
                        tokenData.postValue("error");
                    }
                } else {
                    // When the response isn't successful, post an error.
                    tokenData.postValue("error");
                    Log.e("LoginAPI", "Unsuccessful response: " + response.code()); // Log the error code
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // When the call fails, post an error.
                tokenData.postValue("error");
            }
        });
    }
}


