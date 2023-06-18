package com.example.leaflet_android.repositories;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.leaflet_android.api.ContactAPI;
import com.example.leaflet_android.entities.Contact;

import java.util.LinkedList;
import java.util.List;

public class ContactsRepository {
    //private ContactDao dao;
    private ContactListData contactListData;
    private ContactAPI api;
    public ContactsRepository(){
        //LocalDataBase db = LocalDatabase.getInstance();
        //dao = db.contactDao();
        contactListData = new ContactListData();
//        api = new ContactAPI(contactListData, dao);
    }


    class ContactListData extends MutableLiveData<List<Contact>> {
        public ContactListData() {
            super();
            setValue(new LinkedList<>());

        }

        @Override
        protected void onActive() {
            super.onActive();
//            new Thread(()->
//                // We do postValue when we create a new thread. otherwise it will be get.
//                contactListData.postValue(dao.get());
//            }).start();
            ContactAPI contactAPI = new ContactAPI();
            contactAPI.get(this);
        }

    }

    public LiveData<List<Contact>> getAll() { return contactListData;}

//    public void add (final Contact contact) {api.add(contact);}
//
//    public void delete (final Contact contact) {api.delete(contact);}
//    public void reload() {api.get();}
}