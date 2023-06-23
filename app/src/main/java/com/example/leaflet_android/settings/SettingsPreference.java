package com.example.leaflet_android.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.leaflet_android.R;
import com.example.leaflet_android.repositories.ContactsRepository;

public class SettingsPreference extends PreferenceFragmentCompat {

    private AppSettings appSettings;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        appSettings = new AppSettings(getContext());

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
                if ("light".equals(themeOption)) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else if ("dark".equals(themeOption)) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                appSettings.setTheme(themeOption);
                getActivity().recreate();

                return true;
            });
        }
    }
}
