package com.example.leaflet_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.leaflet_android.databinding.ActivityAddBinding;
import com.example.leaflet_android.databinding.ActivityContactsBinding;

public class AddActivity extends AppCompatActivity {
    private ActivityAddBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button btnSave = binding.btnSave;
        btnSave.setOnClickListener(view -> {
            //Calling the data base to search for the friend.
        });

        // This line will make us to go back to the last window we visit. meaning back to the contact list.
        //finish();
    }
}