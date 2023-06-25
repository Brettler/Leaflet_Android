package com.example.leaflet_android.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.leaflet_android.R;

public class AppSettings {
    private static final String PREFS_NAME = "app_preferences";
    private static final String SERVER_IP_ADDRESS = "server_ip_address";
    private String DEFAULT_IP_ADDRESS;
    private String THEME = "Theme";
    private String DEFAULT_THEME = "Light";
    private SharedPreferences sharedPreferences;

    public AppSettings(Context context) {
        this.DEFAULT_IP_ADDRESS = context.getString(R.string.BaseUrl);
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public String getServerIpAddress() {
        return sharedPreferences.getString(SERVER_IP_ADDRESS, DEFAULT_IP_ADDRESS);
    }

    public interface OnPreferenceChangeListener {
        void onServerIpAddressChanged();
    }

    private OnPreferenceChangeListener listener;

    public void setOnPreferenceChangeListener(OnPreferenceChangeListener listener) {
        this.listener = listener;
    }

    public void setServerIpAddress(String serverIpAddress) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SERVER_IP_ADDRESS, serverIpAddress);
        editor.apply();
        if (listener != null) {
            listener.onServerIpAddressChanged();
        }
    }

//    public void setServerIpAddress(String serverIpAddress) {
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(SERVER_IP_ADDRESS, serverIpAddress);
//        editor.apply();
//    }

    public String getDefaultTheme() {
        return DEFAULT_THEME;
    }



    public String getTheme() {
        return sharedPreferences.getString(THEME, DEFAULT_THEME);
    }

    public void setTheme(String theme) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(THEME, theme);
        editor.apply();
    }

}
