package com.example.leaflet_android.repositories;


import static android.content.Context.MODE_PRIVATE;

import static com.example.leaflet_android.LeafletApp.context;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.leaflet_android.AppDB;
import com.example.leaflet_android.LeafletApp;
import com.example.leaflet_android.api.ContactAPI;
import com.example.leaflet_android.dao.ContactDao;
import com.example.leaflet_android.entities.Contact;

import java.util.List;

public class ContactsRepository {
    private ContactDao dao;
    private ContactListData contactListData;
    private ContactAPI contactAPI;

    public ContactsRepository(){
        AppDB db = AppDB.getDatabase(context);
        dao = db.contactDao();

        contactListData = new ContactListData();
        contactAPI = new ContactAPI(contactListData, dao);

        // If we
        LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                refreshRetrofitInstance();
            }
        }, new IntentFilter("IP_ADDRESS_CHANGED"));
    }

    public void refreshRetrofitInstance() {
        contactAPI.refreshRetrofitInstance();
    }

    class ContactListData extends MutableLiveData<List<Contact>> {
        public ContactListData() {
            super();
        }

        @Override
        protected void onActive() {
            super.onActive();

            // First, load data from local database.
            new Thread(() -> {
                List<Contact> contactsFromDB = dao.index();
                Log.d("ContactListData", "Loaded contacts from database: " + contactsFromDB);
                // Post the value so the UI can update.
                postValue(contactsFromDB);

                // After the local data is loaded, make the network request.
                SharedPreferences sharedPreferences = context.getSharedPreferences("sharedLocal", MODE_PRIVATE);
                String token = sharedPreferences.getString("token", "");
                contactAPI.requestAllContacts(token, this);
            }).start();
        }

    }

    // contactListData is type mutable live data therefore we can return it as livedata.

    public LiveData<List<Contact>> getAll() {
        return contactListData;
    }

    public void addContact (final String username, String token) {
        contactAPI.requestAddContact(username, token);
    }

    public LiveData<String> getErrorLiveData() {
        return contactAPI.getErrorLiveData();
    }


//    public void addTestContact() {
//        Contact testContact = new Contact("456546", "Test User", R.drawable.default_user_pic, "Hello, this is a test message");
//
//        new Thread(() -> {
//            dao.insert(testContact);
//            Log.d("addTestContact", "Inserted test contact into database: " + testContact);
//            // Request an update of the contact list
//            contactListData.onActive();
//        }).start();
//    }

}