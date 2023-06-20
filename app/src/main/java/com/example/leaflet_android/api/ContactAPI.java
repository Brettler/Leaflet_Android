package com.example.leaflet_android.api;

import androidx.lifecycle.MutableLiveData;

import com.example.leaflet_android.dao.ContactDao;
import com.example.leaflet_android.LeafletApp;
import com.example.leaflet_android.R;
import com.example.leaflet_android.entities.Contact;

import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactAPI {
    private MutableLiveData<List<Contact>> contactListData;
    private ContactDao dao;
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;

    public ContactAPI(MutableLiveData<List<Contact>> contactListData, ContactDao dao) {
        this.contactListData = contactListData;
        this.dao = dao;

        retrofit = new Retrofit.Builder()
                .baseUrl(LeafletApp.context.getString(R.string.BaseUrl))
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        webServiceAPI = retrofit.create(WebServiceAPI.class);


    }

    // Retrieve all the contacts from the server
    public void requestAllContacts(String token, MutableLiveData<List<Contact>> contacts) {
        Call<List<Contact>> call = webServiceAPI.getContacts(token);
        call.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    List<Contact> contacts = response.body();
                    contactListData.postValue(contacts);
                    new Thread(() -> {
                        dao.insertAll(contacts);
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                // Handle failure
            }
        });
    }


    // Add contact to the server
    public void requestAddContact(String friendUsername, String token) {
        Call<Contact> call = webServiceAPI.createContact(token, friendUsername);
        call.enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {
                if(response.isSuccessful() && response.body() != null) {
                    // Save the contact to local DB
                    new Thread(() -> {
                        Contact addedContact = response.body();
                        if (addedContact.getLastMessage() == null) {
                            addedContact.setLastMessage("No messages yet");
                        }
                        dao.insert(addedContact);
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {
                // Handle failure
            }
        });
    }
}
