package com.example.leaflet_android.api;

import androidx.lifecycle.MutableLiveData;

import com.example.leaflet_android.LeafletApp;
import com.example.leaflet_android.R;
import com.example.leaflet_android.entities.Contact;
//import com.google.android.gms.common.api.Response;
import retrofit2.Response;

import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactAPI {
    //private MutableLiveData<List<Contact>> postListData;
    //private ContactDao dao;
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;

    //    public ContactAPI(MutableLiveData<List<Contact>> postListData, PostDao dao) {
//        this.postListData = postListData;
//        this.dao = dao;
    public ContactAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(LeafletApp.context.getString(R.string.BaseUrl))
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

//    }

    // Retrive all the contacts from the server
    public void get(MutableLiveData<List<Contact>> contacts) {
        Call<List<Contact>> call = webServiceAPI.getContacts();
        call.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                  contacts.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                // Handle failure
            }
        });
    }
}
