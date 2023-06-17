package com.example.leaflet_android.register;

public interface RegisterHandleServerResponse {
    void onRegistrationSuccess();
    void onUsernameTakenError();
    void onRegistrationFailure();
    void onNetworkError();
}

