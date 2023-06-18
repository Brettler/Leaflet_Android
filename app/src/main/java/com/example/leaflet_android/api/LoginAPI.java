package com.example.leaflet_android.api;

import androidx.lifecycle.MutableLiveData;

import com.example.leaflet_android.LeafletApp;
import com.example.leaflet_android.R;
import com.example.leaflet_android.login.UserLogin;

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

    public LoginAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(LeafletApp.context.getString(R.string.BaseUrl))
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void loginUser(UserLogin user, final MutableLiveData<String> tokenData) {
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
                    } catch (IOException e) {
                        e.printStackTrace();
                        tokenData.postValue("error");
                    }
                } else {
                    // When the response isn't successful, post an error.
                    tokenData.postValue("error");
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


