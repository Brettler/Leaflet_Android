package com.example.leaflet_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.leaflet_android.api.LoginAPI;
import com.example.leaflet_android.databinding.ActivityMainBinding;
import com.example.leaflet_android.login.UserLogin;
import com.example.leaflet_android.viewmodels.LoginViewModel;
import com.example.leaflet_android.viewmodels.UserInfoViewModel;

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


        UserLogin userLogin = new UserLogin("username", "password"); // Use actual values.
        loginViewModel.loginUser(userLogin);


        binding.loginButton.setOnClickListener(v->{
            String username = binding.tvLoginUsername.getEditText().getText().toString();
            String password = binding.tvLoginPassword.getEditText().getText().toString();
            // initiate the login request
            UserLogin user = new UserLogin(username, password);

            loginViewModel.loginUser(user);  // this will initiate the login request

            loginViewModel.getLoginToken().observe(this, token -> {
                if(token != null) {
                    if(token.equals("error")) {
                        Toast.makeText(MainActivity.this, "Invalid username and/or password", Toast.LENGTH_SHORT).show();
                    } else {
                        // Handle the case when a valid token is received
                        // Save the token and navigate to the next Activity
                        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token", token);
                        editor.apply();

                        // Fetch user info now that we have a valid token
                        userInfoViewModel.fetchUserInfo(token, username);

//                        Intent i = new Intent(this, ContactsActivity.class);
//                        startActivity(i);
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
        });

        // If the user didn't register yet, we give him the option.
        binding.btnMoveToRegisterPage.setOnClickListener(v -> {
            Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i);
        });
    }
}