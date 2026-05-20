package com.example.moneymate.data.local.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.moneymate.util.Constants;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class UserPreferences {
    private static final String PREFS_NAME = "moneymate_prefs";
    private final SharedPreferences prefs;

    @Inject
    public UserPreferences(@ApplicationContext Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public String getCurrency() {
        return prefs.getString(Constants.PREF_CURRENCY, Constants.CURRENCY_VND);
    }

    public void setCurrency(String currency) {
        prefs.edit().putString(Constants.PREF_CURRENCY, currency).apply();
    }

    public String getTheme() {
        return prefs.getString(Constants.PREF_THEME, Constants.THEME_SYSTEM);
    }

    public void setTheme(String theme) {
        prefs.edit().putString(Constants.PREF_THEME, theme).apply();
    }

    public boolean isBiometricEnabled() {
        return prefs.getBoolean(Constants.PREF_BIOMETRIC_ENABLED, false);
    }

    public void setBiometricEnabled(boolean enabled) {
        prefs.edit().putBoolean(Constants.PREF_BIOMETRIC_ENABLED, enabled).apply();
    }

    public boolean isAppLockEnabled() {
        return prefs.getBoolean(Constants.PREF_APP_LOCK_ENABLED, false);
    }

    public void setAppLockEnabled(boolean enabled) {
        prefs.edit().putBoolean(Constants.PREF_APP_LOCK_ENABLED, enabled).apply();
    }

    public boolean verifyPin(String pin) {
        String stored = prefs.getString(Constants.PREF_PIN_CODE, null);
        if (stored == null) return false;
        return stored.equals(hashPin(pin));
    }

    public void setPin(String pin) {
        prefs.edit().putString(Constants.PREF_PIN_CODE, hashPin(pin)).apply();
    }

    public boolean hasPin() {
        return prefs.contains(Constants.PREF_PIN_CODE);
    }

    public long getDefaultAccountId() {
        return prefs.getLong(Constants.PREF_DEFAULT_ACCOUNT_ID, -1);
    }

    public void setDefaultAccountId(long id) {
        prefs.edit().putLong(Constants.PREF_DEFAULT_ACCOUNT_ID, id).apply();
    }

    private String hashPin(String pin) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(pin.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return pin;
        }
    }
}
