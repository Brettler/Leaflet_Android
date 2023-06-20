package com.example.leaflet_android.api;
import com.example.leaflet_android.login.UserInfo;
import com.example.leaflet_android.login.UserLogin;
import com.example.leaflet_android.register.UserRegister;
import com.example.leaflet_android.entities.Contact;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebServiceAPI {
    // Register
    @POST("/api/Users")
    Call<Void> createUser(@Body UserRegister user);

    // Login
    @POST("/api/Tokens")
    Call<ResponseBody> userLogin(@Body UserLogin user);

    // Retrieve on the user that logged in.
    @GET("/api/Users/{id}")
    Call<UserInfo> getUserInfo(@Header("Authorization") String token, @Header("Firebase") String firebaseToken, @Path("id") String id);

    // Add friend
    @POST("/api/Chats")
    Call<Contact> createContact(@Header("Authorization") String token, @Body String username);

    // Retrive all the contacts (chats list)
    @GET("/api/Chats")
    Call<List<Contact>> getContacts(@Header("Authorization") String token);

    @DELETE("/api/Chats/{id}")
    Call<Void> deleteContact(@Path("id") int id);
}
