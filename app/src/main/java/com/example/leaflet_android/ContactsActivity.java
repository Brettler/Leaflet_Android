package com.example.leaflet_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leaflet_android.adapters.ContactsListAdapter;
import com.example.leaflet_android.databinding.ActivityContactsBinding;
import com.example.leaflet_android.viewmodels.ContactsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ContactsActivity extends AppCompatActivity {
    private ActivityContactsBinding binding;
    private ContactsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityContactsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(ContactsViewModel.class);

        RecyclerView lstContacts = binding.lstContacts;
        final ContactsListAdapter adapter = new ContactsListAdapter(this);
        lstContacts.setAdapter(adapter);
        lstContacts.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton btnOptions = binding.btnOptions;

        btnOptions.setOnClickListener(view -> {
            // Create a new instance of PopupMenu
            PopupMenu popup = new PopupMenu(ContactsActivity.this, view);

            // Inflate the popup menu
            popup.getMenuInflater().inflate(R.menu.options_menu, popup.getMenu());

            // Add click listener for menu items
            popup.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.menu_add) {
                    Toast.makeText(this, "Moving to the AddActivity", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(ContactsActivity.this, AddActivity.class);
                    startActivity(i);
                    return true;
                } else if (id == R.id.menu_profile) {
                    Toast.makeText(this, "Moving to the User Profile", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(ContactsActivity.this, ProfileActivity.class);
                    startActivity(i);
                    return true;
                } else if (id == R.id.menu_settings) {
                    Toast.makeText(this, "Moving to the Settings", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(ContactsActivity.this, SettingsActivity.class);
                    startActivity(i);

                    return true;
                }
                return false;
            });

            popup.show();
        });

        // Each time the viewModel changes we will refresh the contact list.
        viewModel.get().observe(this, contacts -> {
            Log.d("ContactsActivity", "Updating contacts in adapter: " + contacts);
            adapter.setContacts(contacts);
        });
    }
}