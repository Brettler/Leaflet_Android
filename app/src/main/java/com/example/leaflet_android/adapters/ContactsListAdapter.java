package com.example.leaflet_android.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leaflet_android.entities.Contact;

import java.util.List;

public class ContactsListAdapter {

    private final LayoutInflater mInflater;
    private List<Contact> contacts;
    public ContactsListAdapter(Context contect) {
        mInflater = LayoutInflater.from(contect);
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.contact_item, parent, false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder()

}
