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
    @GET("/api/Chats")
    Call<List<Contact>> getContacts();
    @POST("/api/Chats")
    Call<Void> creatContact(@Body Contact contact);

    @DELETE("/api/Chats/{id}")
    Call<Void> deleteContact(@Path("id") int id);

    // Register
    @POST("/api/Users")
    Call<Void> createUser(@Body UserRegister user);

    // Login
    @POST("/api/Tokens")
    Call<ResponseBody> userLogin(@Body UserLogin user);

    // Retrive on the user that logged in.
    @GET("/api/Users/{id}")
    Call<UserInfo> getUserInfo(@Header("Authorization") String token, @Path("id") String id);
}
