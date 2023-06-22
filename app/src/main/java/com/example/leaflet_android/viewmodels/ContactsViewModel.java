package com.example.leaflet_android.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.leaflet_android.entities.Contact;
import com.example.leaflet_android.repositories.ContactsRepository;

import java.util.List;

public class ContactsViewModel extends ViewModel {
    private static final String TAG = "ContactsViewModel";

    private ContactsRepository mRepository;
    private LiveData<List<Contact>> contacts;

    public ContactsViewModel() {
        Log.d(TAG, "Creating new ContactsViewModel");
        mRepository = new ContactsRepository();
        // Requesting all the contacts of the user
        contacts = mRepository.getAll();
        Log.d(TAG, "Contacts from repository: " + contacts.getValue());

    }

    // get expose the contacts varaible to the classes who use it.
    public LiveData<List<Contact>> get() {
        Log.d(TAG, "Getting contacts: " + contacts.getValue());
        return contacts;
    }

    // Need to implement them with the API.

    public void addContact(final String username, String token) {
        Log.d(TAG, "Adding contact: " + username);
        mRepository.addContact(username, token);
    }
    public LiveData<String> getErrorLiveData() {
        LiveData<String> errorLiveData = mRepository.getErrorLiveData();
        Log.d(TAG, "Getting error LiveData: " + errorLiveData.getValue());
        return errorLiveData;
    }

//    public void addTestContact() {
//        Log.d(TAG, "Adding test contact");
//        mRepository.addTestContact();
//    }
}
//    public void delete(Contact post) {
//        mRepository.delete(contact);
//    }
//    public void reload() {
//        mRepository.reload();
//    }

