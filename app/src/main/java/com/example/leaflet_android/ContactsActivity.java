package com.example.leaflet_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leaflet_android.adapters.ContactsListAdapter;
import com.example.leaflet_android.dao.ContactDao;
import com.example.leaflet_android.databinding.ActivityContactsBinding;
import com.example.leaflet_android.viewmodels.ContactsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ContactsActivity extends AppCompatActivity {
    private ActivityContactsBinding binding;
    private ContactsViewModel viewModel;
    private AppDB db;
    private ContactDao contactDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityContactsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //This line problamtic.
        viewModel = new ViewModelProvider(this).get(ContactsViewModel.class);

        RecyclerView lstContacts = binding.lstContacts;
        final ContactsListAdapter adapter = new ContactsListAdapter(this);
        lstContacts.setAdapter(adapter);
        lstContacts.setLayoutManager(new LinearLayoutManager(this));



        // We need to change it later to switch from '.allowMainThreadQueries()' to operate thread.
        db = AppDB.getDatabase(this);
        contactDao = db.contactDao();

        // Now we want to retrieve the contactList from the local DB until the server will respond if we have any new friends that added us or sent as messages.

        // When the user click on the add friend button he can add a friend.
        FloatingActionButton btnAdd = binding.btnAdd;
        btnAdd.setOnClickListener(view -> {
            Toast.makeText(this, "Moving to the AddActivity", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(ContactsActivity.this, AddActivity.class);
            startActivity(i);
        });


        // Each time the viewModel changes we will refresh the contact list.
        viewModel.get().observe(this, contacts -> {
            adapter.setContacts(contacts);
        });
    }
}