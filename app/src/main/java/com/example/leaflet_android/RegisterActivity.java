package com.example.leaflet_android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.leaflet_android.api.RegisterAPI;
import com.example.leaflet_android.databinding.ActivityRegisterBinding;
import com.example.leaflet_android.register.RegisterPasswordHandler;
import com.example.leaflet_android.register.UserRegister;
import com.example.leaflet_android.viewmodels.RegisterViewModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;

    // Password
    private RegisterPasswordHandler registerPasswordHandler;
    // Images
    private static final int PICK_IMAGE = 1;
    private Uri selectedImageUri = null;
    // Server
    private RegisterViewModel registerViewModel;

    private RegisterAPI registerAPI;


    // Define the ActivityResultLauncher
    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                    selectedImageUri = uri; // set selectedImageUri here
                    binding.profileImageView.setImageURI(uri);
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Hides the Action Bar or the Header
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        binding.btnMoveToLoginPage.setOnClickListener(v -> {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        });


        // Create variable and instance to handle user password.
        registerPasswordHandler = new RegisterPasswordHandler(
                binding.editTextTextPassword,
                binding.editTextTextVerifyPassword,
                binding.tvRegisterPassword,
                binding.tvVerifyPassword
        );

        // Handle select profile picture event:
        // Choose Image Button
        binding.chooseImageButton.setOnClickListener(v -> {
            // Open Gallery
            mGetContent.launch("image/*");
        });

        registerAPI = new RegisterAPI();  // create instance of RegisterAPI to handle sending all the user information to the server.

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        // Observe LiveData
        registerViewModel.getRegistrationStatus().observe(this, status -> {
            if ("Success".equals(status)) {
                // If registration is successful, navigate to MainActivity
                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(i);
            } else {
                // If registration failed, show error message
                Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Register button click event
        binding.registerButton.setOnClickListener(v -> {
            String username = binding.editTextTextPersonName.getText().toString().trim();
            String password = binding.editTextTextPassword.getText().toString().trim();
            String displayName = binding.editTextTextDisplayName.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty() || displayName.isEmpty()) {
                // Display an error if any field is empty
                Toast.makeText(RegisterActivity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            } else {
                // create new user instance from the form inputs
                UserRegister user = new UserRegister(
                        username,
                        password,
                        displayName,
                        selectedImageUri != null ? getImageBase64(selectedImageUri) : getDefaultImageBase64("default_user_pic")
                );
                // call the registerUser method with the created user
                registerViewModel.registerUser(user);
            }
        });
    }

    // Handle retrieve the image from the user.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            binding.profileImageView.setImageURI(selectedImageUri);
        }
    }
    private String getImageBase64(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

            // Define a maximum height and width
            final int maxSize = 1024;
            int outWidth;
            int outHeight;
            int inWidth = bitmap.getWidth();
            int inHeight = bitmap.getHeight();
            if(inWidth > inHeight){
                outWidth = maxSize;
                outHeight = (inHeight * maxSize) / inWidth;
            } else {
                outHeight = maxSize;
                outWidth = (inWidth * maxSize) / inHeight;
            }

            // Create a scaled bitmap
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, false);

            // Compress the bitmap in JPEG format at 75% quality
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private String getDefaultImageBase64(String drawableResourceName) {
        try {
            // get the drawable resource
            int drawableResourceId = this.getResources().getIdentifier(drawableResourceName, "drawable", this.getPackageName());

            // convert drawable to bitmap
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableResourceId);

            // Define a maximum height and width
            final int maxSize = 1024;
            int outWidth;
            int outHeight;
            int inWidth = bitmap.getWidth();
            int inHeight = bitmap.getHeight();
            if(inWidth > inHeight){
                outWidth = maxSize;
                outHeight = (inHeight * maxSize) / inWidth;
            } else {
                outHeight = maxSize;
                outWidth = (inWidth * maxSize) / inHeight;
            }

            // Create a scaled bitmap
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, false);

            // Compress the bitmap in JPEG format at 75% quality
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    // Handle sending the server user register information.



}