package com.example.leaflet_android.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.leaflet_android.R;
import com.example.leaflet_android.entities.Contact;
import com.example.leaflet_android.repositories.ContactsRepository;

import java.util.ArrayList;
import java.util.List;

public class ContactsViewModel extends ViewModel {

    private ContactsRepository mRepository;

    private LiveData<List<Contact>> contacts;


    public ContactsViewModel(ContactsRepository mRepository, LiveData<List<Contact>> contacts) {
        mRepository = new ContactsRepository();
        // Requesting all the contacts of the user
        contacts = mRepository.getAll();

//        contacts.add(new Contact("Liad", R.drawable.default_user_pic, "Hello message"));
//        contacts.add(new Contact("Eden", R.drawable.baseline_person_24, "Hello also!"));

    }

    // get expose the contacts varaible to the classes who use it.
    public LiveData<List<Contact>> get() {
        return contacts;
    }

    // Need to implement them with the API.

//    public void add(Contact contact) {
//        mRepository.add(contact);
//    }
//    public void delete(Contact post) {
//        mRepository.delete(contact);
//    }
//    public void reload() {
//        mRepository.reload();
//    }

}
