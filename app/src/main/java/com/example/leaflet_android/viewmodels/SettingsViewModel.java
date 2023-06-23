package com.example.leaflet_android.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingsViewModel extends ViewModel {

    private final MutableLiveData<Boolean> themeChangeFlag = new MutableLiveData<>();

    public void triggerThemeChange() {
        themeChangeFlag.setValue(true);
    }

    public LiveData<Boolean> getThemeChangeFlag() {
        return themeChangeFlag;
    }

    public void resetThemeChangeFlag() {
        themeChangeFlag.setValue(false);
    }

}
