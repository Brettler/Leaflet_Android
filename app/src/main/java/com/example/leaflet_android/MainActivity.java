package com.example.leaflet_android;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.leaflet_android.activities.ContactsActivity;
import com.example.leaflet_android.activities.RegisterActivity;
import com.example.leaflet_android.databinding.ActivityMainBinding;
import com.example.leaflet_android.login.UserLogin;
import com.example.leaflet_android.settings.AppSettings;
import com.example.leaflet_android.viewmodels.LoginViewModel;
import com.example.leaflet_android.viewmodels.UserInfoViewModel;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    // Firebase
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                    Toast.makeText(MainActivity.this, "You will receive notifications.", Toast.LENGTH_SHORT).show();
                } else {
                    // Inform user that your app will not show notifications.
                    Toast.makeText(MainActivity.this, "Without notification permission, you won't receive updates.", Toast.LENGTH_SHORT).show();
                }
            });
    private void askNotificationPermission() {
        // Check if shouldShowRequestPermissionRationale() is true
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_NOTIFICATION_POLICY)) {
            // Show an explanation to the user
            new AlertDialog.Builder(this)
                    .setTitle("Notification Permission Required")
                    .setMessage("This app needs the Notification permission to send you updates. Would you like to grant it?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // request the permission
                        requestPermissionLauncher.launch(Manifest.permission.ACCESS_NOTIFICATION_POLICY);
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .create().show();
        } else {
            // No explanation needed; request the permission
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_NOTIFICATION_POLICY);
        }
    }
    private ActivityMainBinding binding;
    private LoginViewModel loginViewModel;
    private UserInfoViewModel userInfoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppSettings appSettings = new AppSettings(this);
        String theme = appSettings.getTheme();

        if ("Light".equalsIgnoreCase(theme)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if ("Dark".equalsIgnoreCase(theme)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        setTitle("Welcome To Leaflet");

        SharedPreferences sharedPreferences = getSharedPreferences("sharedLocal", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            Intent i = new Intent(MainActivity.this, ContactsActivity.class);
            startActivity(i);
            finish();
            return;
        }

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        userInfoViewModel = new ViewModelProvider(this).get(UserInfoViewModel.class);

        // Observers
        loginViewModel.getLoginToken().observe(this, token -> {
            if (token != null) {
                if (!token.equals("error")) {
                    // Save the token and Try to fetch also tokenFCM from Firebase.
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", token);
                    editor.apply();


                    // Retrieve the current registration token.
                    FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Log.w("FirebaseMainActivity", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token for each user that installs and uses the app.
                        String firebaseToken = task.getResult();

                        // Fetch user info now that we have a valid token
                        String username = binding.tvLoginUsername.getEditText().getText().toString();
                        if (!username.isEmpty()) {
                            // Make the request from the server to get the information about the user
                            // that just logged in.
                            userInfoViewModel.fetchUserInfo(token, firebaseToken, username);
                            // Save the username
                            editor.putString("username", username);
                            editor.apply();
                        } else {
                            Toast.makeText(MainActivity.this, "Invalid username", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Invalid username and/or password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Error logging in", Toast.LENGTH_SHORT).show();
            }
        });

        userInfoViewModel.getUserInfo().observe(this, userInfo -> {
            if (userInfo != null) {
                // Store user info in the local database
                userInfoViewModel.storeUserInfo(userInfo);

                // Set isLoggedIn to true only after successfully fetching the user info
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", true);

                editor.apply();
                // Ask for notification permission after successful login
                askNotificationPermission();
                // Navigate to the next Activity
                Intent i = new Intent(MainActivity.this, ContactsActivity.class);
                startActivity(i);
                finish(); // finish current activity


            } else {
                Toast.makeText(MainActivity.this, "Failed to fetch user info", Toast.LENGTH_SHORT).show();
            }
        });

        binding.loginButton.setOnClickListener(v -> {
            String username = binding.tvLoginUsername.getEditText().getText().toString();
            String password = binding.tvLoginPassword.getEditText().getText().toString();
            // initiate the login request
            UserLogin user = new UserLogin(username, password);
            loginViewModel.loginUser(user);  // this will initiate the login request
        });


        // If the user didn't register yet, we give him the option.
        binding.btnMoveToRegisterPage.setOnClickListener(v -> {
            Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i);
        });


        // Add click listener for the server settings button
        binding.serverSettingsIP.setOnClickListener(v -> showServerIpDialog());
        Animation animation = AnimationUtils.loadAnimation(LeafletApp.context, R.anim.scale_animiation);
        binding.serverSettingsIP.startAnimation(animation);


    }

    private void showServerIpDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.login_set_server_ip, null);

        final EditText editTextServerIp = view.findViewById(R.id.edit_text_server_ip);

        AppSettings appSettings = new AppSettings(MainActivity.this);
        String currentIp = appSettings.getServerIpAddress();

        editTextServerIp.setText(currentIp);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Change Server IP")
                .setView(view)
                .setPositiveButton("Change", (dialog1, which) -> {
                    String newIp = String.valueOf(editTextServerIp.getText());
                    appSettings.setServerIpAddress(newIp);
                    // You may need to recreate your LoginAPI instance or somehow let it know that the IP address has changed
                })
                .setNegativeButton("Cancel", null)
                .create();

        dialog.show();
    }

}