package com.example.leaflet_android.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.leaflet_android.api.RegisterAPI;
import com.example.leaflet_android.register.UserRegister;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterViewModel extends ViewModel {
    private MutableLiveData<String> registrationStatusLiveData = new MutableLiveData<>();

    public LiveData<String> getRegistrationStatus() {
        return registrationStatusLiveData;
    }

    public void registerUser(UserRegister user) {
        RegisterAPI registerAPI = new RegisterAPI();
        registerAPI.registerUser(user, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    registrationStatusLiveData.postValue("Success");
                } else {
                    if (response.code() == 409) {
                        registrationStatusLiveData.postValue("This username is already taken.");
                    } else {
                        registrationStatusLiveData.postValue("Failed to register");
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("RegisterAPI", "Error: " + t.getMessage());
                registrationStatusLiveData.postValue("Network error, please try again later.");
            }
        });
    }
}
