package com.example.leaflet_android.register;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterPasswordHandler {
    private final TextInputEditText passwordEditText;
    private final TextInputEditText verifyPasswordEditText;
    private final TextInputLayout passwordLayout;
    private final TextInputLayout verifyPasswordLayout;

    public RegisterPasswordHandler(TextInputEditText passwordEditText,
                                   TextInputEditText verifyPasswordEditText,
                                   TextInputLayout passwordLayout,
                                   TextInputLayout verifyPasswordLayout) {
        this.passwordEditText = passwordEditText;
        this.verifyPasswordEditText = verifyPasswordEditText;
        this.passwordLayout = passwordLayout;
        this.verifyPasswordLayout = verifyPasswordLayout;

        // Check password on each change
        this.passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkPassword();
                // Also, verify if both password and verifyPassword match after changing password
                checkVerifyPassword(verifyPasswordEditText.getText().toString());
            }
        });

        // Check verify password on each change
        this.verifyPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkVerifyPassword(s.toString());
            }
        });
    }

    public void checkPassword() {
        String password = passwordEditText.getText().toString();

        if (password.isEmpty()) {
            passwordLayout.setError("Password is required");
        } else if (password.length() < 5) {
            passwordLayout.setError("Password must be at least 5 characters long");
        } else if (!password.matches(".*[a-z].*")) {
            passwordLayout.setError("Password must contain at least one lowercase letter");
        } else if (!password.matches(".*[A-Z].*")) {
            passwordLayout.setError("Password must contain at least one uppercase letter");
        } else if (!password.matches(".*\\d.*")) {
            passwordLayout.setError("Password must contain at least one digit");
        } else {
            passwordLayout.setError(null); // Clear the error if it passed the validation
        }
    }

    public void checkVerifyPassword(String verifyPassword) {
        String password = passwordEditText.getText().toString();

        if (!verifyPassword.isEmpty() && !verifyPassword.equals(password)) {
            verifyPasswordLayout.setError("Passwords do not match");
        } else {
            verifyPasswordLayout.setError(null); // Clear the error if it passed the validation
        }
    }
}


