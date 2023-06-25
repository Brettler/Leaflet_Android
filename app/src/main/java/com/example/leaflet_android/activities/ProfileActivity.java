package com.example.leaflet_android.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.leaflet_android.databinding.ActivityProfileBinding;
import com.example.leaflet_android.viewmodels.UserInfoViewModel;

public class ProfileActivity extends AppCompatActivity {
    private ImageView profileImageView;
    private TextView displayNameTextView;
    private TextView userNameTextView;

    private ActivityProfileBinding binding;
    private UserInfoViewModel userInfoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_profile);

        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        setTitle("Here is your Beautiful Profile");


        profileImageView =binding.profileImageView;
        displayNameTextView = binding.tvDisplayName;
        userNameTextView = binding.tvUserName;


        userInfoViewModel = new ViewModelProvider(this).get(UserInfoViewModel.class);

        SharedPreferences sharedPreferences = getSharedPreferences("sharedLocal", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);

        userInfoViewModel = new ViewModelProvider(this).get(UserInfoViewModel.class);

        if(username != null) {
            userInfoViewModel.getUserInfoFromDB(username).observe(this, userInfo -> {
                if(userInfo != null) {
                    displayNameTextView.setText("Displayname: " + userInfo.getDisplayName());
                    userNameTextView.setText("Username: " + userInfo.getUsername());

                    // Using image loading library for Android, to present the user his profile picutre.
                    Glide.with(this)
                            .load(userInfo.getProfilePic())
                            .into(profileImageView);
                }
            });
        }
    }
}