package com.example.leaflet_android;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.leaflet_android.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnMoveToRegisterPage.setOnClickListener(v -> {
            Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i);
        });

        binding.loginButton.setOnClickListener(v->{
            Intent i = new Intent(this, ContactsActivity.class);
            startActivity(i);
        });


        setTitle("Welcome To Leaflet");

    }
}