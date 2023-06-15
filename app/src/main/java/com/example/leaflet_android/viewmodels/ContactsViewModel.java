package com.example.leaflet_android.viewmodels;

import androidx.lifecycle.LiveData;

import com.example.leaflet_android.entities.Contact;

import java.util.List;

public class ContactsViewModel {

    private ContactsRepository mRepository;

    private LiveData<List<Conatct>> contacts;


    public ContactsViewModel(ContactsRepository mRepository, LiveData<List<Conatct>> contacts) {
        this.mRepository = new ContactsRepository();
        this.contacts = contacts.getAll();
    }
    public LiveData<List<Contact>> get() {
        return contacts;
    }
    public void add(Contact contact) {
        mRepository.add(contact);
    }
    public void delete(Contact post) {
        mRepository.delete(contact);
    }
    public void reload() {
        mRepository.reload();
    }

}
