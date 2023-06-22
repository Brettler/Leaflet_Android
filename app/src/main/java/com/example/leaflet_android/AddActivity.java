package com.example.leaflet_android;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.leaflet_android.databinding.ActivityAddBinding;
import com.example.leaflet_android.viewmodels.ContactsViewModel;

public class AddActivity extends AppCompatActivity {
    private ActivityAddBinding binding;
    private ContactsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Button btnSave = binding.btnSave;
        viewModel = new ViewModelProvider(this).get(ContactsViewModel.class);
        btnSave.setOnClickListener(view -> {
            String friendUsername = binding.etContactUsername.getText().toString();
            // Retrieve token from shared preferences
            SharedPreferences sharedPreferences = getSharedPreferences("sharedLocal", MODE_PRIVATE);
            String token = sharedPreferences.getString("token", "");
            if (!token.equals("")) {
                // Request from the server adding a friend.
                viewModel.addContact(friendUsername, token);
            } else {
                // Handle error - token not found
            }
            finish();
        });

        viewModel.getErrorLiveData().observe(this, errorMessage -> {
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        });


    }
}