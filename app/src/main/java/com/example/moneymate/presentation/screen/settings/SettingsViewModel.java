package com.example.moneymate.presentation.screen.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moneymate.data.local.preferences.UserPreferences;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SettingsViewModel extends ViewModel {
    private final UserPreferences userPreferences;

    private final MutableLiveData<String> currency = new MutableLiveData<>();
    private final MutableLiveData<String> theme = new MutableLiveData<>();
    private final MutableLiveData<Boolean> biometricEnabled = new MutableLiveData<>();
    private final MutableLiveData<Boolean> appLockEnabled = new MutableLiveData<>();
    private final MutableLiveData<Boolean> pinSaved = new MutableLiveData<>();

    @Inject
    public SettingsViewModel(UserPreferences userPreferences) {
        this.userPreferences = userPreferences;
        loadSettings();
    }

    private void loadSettings() {
        currency.setValue(userPreferences.getCurrency());
        theme.setValue(userPreferences.getTheme());
        biometricEnabled.setValue(userPreferences.isBiometricEnabled());
        appLockEnabled.setValue(userPreferences.isAppLockEnabled());
    }

    public void setCurrency(String curr) {
        userPreferences.setCurrency(curr);
        currency.setValue(curr);
    }

    public void setTheme(String t) {
        userPreferences.setTheme(t);
        theme.setValue(t);
    }

    public void setBiometricEnabled(boolean enabled) {
        userPreferences.setBiometricEnabled(enabled);
        biometricEnabled.setValue(enabled);
    }

    public void setAppLockEnabled(boolean enabled) {
        userPreferences.setAppLockEnabled(enabled);
        appLockEnabled.setValue(enabled);
    }

    public void savePin(String pin) {
        userPreferences.setPin(pin);
        pinSaved.setValue(true);
    }

    public boolean verifyPin(String pin) {
        return userPreferences.verifyPin(pin);
    }

    public boolean hasPin() {
        return userPreferences.hasPin();
    }

    public LiveData<String> getCurrency() { return currency; }
    public LiveData<String> getTheme() { return theme; }
    public LiveData<Boolean> getBiometricEnabled() { return biometricEnabled; }
    public LiveData<Boolean> getAppLockEnabled() { return appLockEnabled; }
    public LiveData<Boolean> getPinSaved() { return pinSaved; }
}
