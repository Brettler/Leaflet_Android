package com.example.leaflet_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.leaflet_android.databinding.ActivityMainBinding;
import com.example.leaflet_android.login.UserLogin;
import com.example.leaflet_android.settings.AppSettings;
import com.example.leaflet_android.viewmodels.LoginViewModel;
import com.example.leaflet_android.viewmodels.UserInfoViewModel;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private LoginViewModel loginViewModel;
    private UserInfoViewModel userInfoViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Welcome To Leaflet");

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        userInfoViewModel = new ViewModelProvider(this).get(UserInfoViewModel.class);

        // Observers
        loginViewModel.getLoginToken().observe(this, token -> {
            if(token != null) {
                if(!token.equals("error")) {
                    // Save the token and navigate to the next Activity
                    SharedPreferences sharedPreferences = getSharedPreferences("sharedLocal", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", token);
                    editor.apply();


                    FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            // Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token for each user that installs and uses the app.
                        String firebaseToken = task.getResult();

                        // Fetch user info now that we have a valid token
                        String username = binding.tvLoginUsername.getEditText().getText().toString();
                        if(!username.isEmpty()) {
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

                // Navigate to the next Activity
                Intent i = new Intent(MainActivity.this, ContactsActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(MainActivity.this, "Failed to fetch user info", Toast.LENGTH_SHORT).show();
            }
        });

        binding.loginButton.setOnClickListener(v->{
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