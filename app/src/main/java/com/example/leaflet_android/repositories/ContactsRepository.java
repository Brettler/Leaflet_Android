package com.example.leaflet_android.repositories;


import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.leaflet_android.AppDB;
import com.example.leaflet_android.dao.ContactDao;
import com.example.leaflet_android.LeafletApp;
import com.example.leaflet_android.api.ContactAPI;
import com.example.leaflet_android.entities.Contact;

import java.util.LinkedList;
import java.util.List;

public class ContactsRepository {
    private ContactDao dao;
    private ContactListData contactListData;
    private ContactAPI contactAPI;

    public ContactsRepository(){
        AppDB db = AppDB.getDatabase(LeafletApp.context);
        dao = db.contactDao();

        contactListData = new ContactListData();
        contactAPI = new ContactAPI(contactListData, dao);
    }

    class ContactListData extends MutableLiveData<List<Contact>> {
        public ContactListData() {
            super();

            // Here we need to implement to retrive the contact list from the local data.
            setValue(new LinkedList<>());


        }

        @Override
        protected void onActive() {
            super.onActive();

            // First, load data from local database.
            new Thread(() -> {
                List<Contact> contactsFromDB = dao.index();

                // Post the value so the UI can update.
                postValue(contactsFromDB);

                // After the local data is loaded, make the network request.
                SharedPreferences sharedPreferences = LeafletApp.context.getSharedPreferences("sharedLocal", MODE_PRIVATE);
                String token = sharedPreferences.getString("token", "");
                contactAPI.requestAllContacts(token, this);
            }).start();
        }

    }

    // contactListData is type mutable live data therefore we can return it as livedata.

    public LiveData<List<Contact>> getAll() {
        return contactListData;
    }

    public void add (final String username, String token) {
        contactAPI.requestAddContact(username, token);
    }
}