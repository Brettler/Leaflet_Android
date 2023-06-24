package com.example.leaflet_android.settings;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.leaflet_android.AppDB;
import com.example.leaflet_android.LeafletApp;
import com.example.leaflet_android.MainActivity;
import com.example.leaflet_android.R;

public class SettingsPreference extends PreferenceFragmentCompat {

    private AppSettings appSettings;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        appSettings = new AppSettings(LeafletApp.context);

        // Retrieve and update the server IP address preference
        EditTextPreference serverIPPref = findPreference("signature");
        if (serverIPPref != null) {
            serverIPPref.setText(appSettings.getServerIpAddress());
            serverIPPref.setOnPreferenceChangeListener((preference, newValue) -> {
                String newIpAddress = (String) newValue;
                appSettings.setServerIpAddress(newIpAddress);
                return true;
            });
        }
        // In your SettingsPreference class
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent("IP_ADDRESS_CHANGED"));


        // Retrieve and set the theme preference
        ListPreference themePreference = findPreference("theme_preference");
        if (themePreference != null) {
            themePreference.setValue(appSettings.getTheme());
            themePreference.setOnPreferenceChangeListener((preference, newValue) -> {
                String themeOption = (String) newValue;
                if ("Light".equalsIgnoreCase(themeOption)) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else if ("Dark".equalsIgnoreCase(themeOption)) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                appSettings.setTheme(themeOption);
                getActivity().recreate();

                return true;
            });
        }

        // Retrieve the logout preference and set the click listener
        Preference logoutPreference = findPreference("logout");
        if (logoutPreference != null) {
            logoutPreference.setOnPreferenceClickListener(preference -> {
                new AlertDialog.Builder(getContext())
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout? Your local data will be deleted, but not the data in the server.")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> logout())
                        .setNegativeButton(android.R.string.no, null).show();

                return true;
            });
        }
    }

    // This method will be called when user confirmed logout
    private void logout() {
        // Clear local data here
        new Thread(() -> {
            AppDB.getDatabase(getContext()).clearAllTables();

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sharedLocal", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            // Switch back to UI thread to launch MainActivity
            getActivity().runOnUiThread(() -> {
                // Then start the LoginActivity
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                // Finish the current activity to prevent returning to it when back is pressed
                getActivity().finish();
            });
        }).start();
    }
}