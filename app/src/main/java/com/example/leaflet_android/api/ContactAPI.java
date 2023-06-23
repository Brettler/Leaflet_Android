package com.example.leaflet_android.api;

import static android.content.Context.MODE_PRIVATE;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.leaflet_android.LeafletApp;
import com.example.leaflet_android.R;
import com.example.leaflet_android.chat.ContactUsername;
import com.example.leaflet_android.dao.ContactDao;
import com.example.leaflet_android.entities.Contact;
import com.example.leaflet_android.settings.AppSettings;

import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactAPI {
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    private MutableLiveData<List<Contact>> contactListData;
    private ContactDao dao;
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void refreshRetrofitInstance() {
        AppSettings appSettings = new AppSettings(LeafletApp.context);
        retrofit = new Retrofit.Builder()
                .baseUrl(appSettings.getServerIpAddress())
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }


    public ContactAPI(MutableLiveData<List<Contact>> contactListData, ContactDao dao) {
        this.contactListData = contactListData;
        this.dao = dao;
        refreshRetrofitInstance();
    }


    // Retrieve all the contacts from the server
    public void requestAllContacts(String token, MutableLiveData<List<Contact>> contactsLiveData) {
        Call<List<Contact>> call = webServiceAPI.getContacts("Bearer " + token);
        call.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                if(response.isSuccessful()) {
                    List<Contact> contacts = response.body();
                    Log.d("ContactAPI", "requestAllContacts response: " + contacts);
                    contactsLiveData.postValue(contacts);
                    new Thread(() -> {
                        dao.insertAll(contacts);
                    }).start();
                }
                else {
                    Log.e("requestAllContacts", "Unsuccessful response: " + response.code()); // Log the error code
                }
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                // Handle failure
                Log.e("requestAllContacts", "Request failed: " + t.getMessage()); // Log the failure reason

            }
        });
    }


    // Add contact to the server
    public void requestAddContact(String friendUsername, String token) {

        Call<Contact> call = webServiceAPI.createContact("Bearer " + token, new ContactUsername(friendUsername));
        call.enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {
                if(response.isSuccessful() && response.body() != null) {
                    // Save the contact to local DB
                    Contact addedContact = response.body();
                    Log.d("ContactAPI", "Adding contact response: " + addedContact);

                    //if (addedContact != null) {
                        //if (addedContact.getLastMessage() == null) {
                            //LastMessage noMessage = new LastMessage("8787", "27.2.3", "jesusfuck");
                            //noMessage.setContent("No messages yet");
                            //addedContact.setLastMessage(noMessage);
                        //}
                        //}

                        dao.insert(addedContact);

                } else {
                    Log.e("requestAddContact", "Unsuccessful response: " + response.code());
                    if (response.code() == 400) {
                        errorLiveData.postValue("Users can't add themselves as friends.");
                    } else {
                        errorLiveData.postValue("There is no such Username in the system.");
                    }
                }
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {
                errorLiveData.postValue("Network Error. Please provide better wifi connection.");

                // Handle failure
                Log.e("requestAddContact", "Request failed: " + t.getMessage()); // Log the failure reason

            }
        });
    }
}
