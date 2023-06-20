package com.example.leaflet_android.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.leaflet_android.entities.Contact;
import com.example.leaflet_android.repositories.ContactsRepository;

import java.util.List;

public class ContactsViewModel extends ViewModel {
    private ContactsRepository mRepository;
    private LiveData<List<Contact>> contacts;

    public ContactsViewModel() {
        mRepository = new ContactsRepository();
        // Requesting all the contacts of the user
        contacts = mRepository.getAll();

    }

    // get expose the contacts varaible to the classes who use it.
    public LiveData<List<Contact>> get() {
        return contacts;
    }

    // Need to implement them with the API.

    public void add(final String username, String token) {
        mRepository.add(username, token);
    }
}
//    public void delete(Contact post) {
//        mRepository.delete(contact);
//    }
//    public void reload() {
//        mRepository.reload();
//    }

