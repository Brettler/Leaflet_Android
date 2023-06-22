package com.example.leaflet_android.settings;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSettings {
    private static final String PREFS_NAME = "app_preferences";
    private static final String SERVER_IP_ADDRESS = "server_ip_address";
    private static final String DEFAULT_IP_ADDRESS = "http://192.168.0.106:4656/api/";
    private static final String THEME = "theme";
    private static final String DEFAULT_THEME = "Light";
    private SharedPreferences sharedPreferences;

    public AppSettings(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public String getServerIpAddress() {
        return sharedPreferences.getString(SERVER_IP_ADDRESS, DEFAULT_IP_ADDRESS);
    }

    public void setServerIpAddress(String serverIpAddress) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SERVER_IP_ADDRESS, serverIpAddress);
        editor.apply();
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
