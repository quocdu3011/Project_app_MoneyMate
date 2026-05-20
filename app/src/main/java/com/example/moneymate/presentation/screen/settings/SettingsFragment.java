package com.example.moneymate.presentation.screen.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.moneymate.databinding.FragmentSettingsBinding;
import com.example.moneymate.util.Constants;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;
    private SettingsViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        observeViewModel();
        setupListeners();
    }

    private void observeViewModel() {
        viewModel.getTheme().observe(getViewLifecycleOwner(), theme -> {
            if (Constants.THEME_LIGHT.equals(theme)) binding.rgTheme.check(binding.rbLight.getId());
            else if (Constants.THEME_DARK.equals(theme)) binding.rgTheme.check(binding.rbDark.getId());
            else binding.rgTheme.check(binding.rbSystem.getId());
        });

        viewModel.getBiometricEnabled().observe(getViewLifecycleOwner(), enabled ->
            binding.switchBiometric.setChecked(enabled));

        viewModel.getAppLockEnabled().observe(getViewLifecycleOwner(), enabled ->
            binding.switchAppLock.setChecked(enabled));

        viewModel.getCurrency().observe(getViewLifecycleOwner(), currency -> {
            if (Constants.CURRENCY_USD.equals(currency)) binding.rgCurrency.check(binding.rbUsd.getId());
            else binding.rgCurrency.check(binding.rbVnd.getId());
        });
    }

    private void setupListeners() {
        binding.rgTheme.setOnCheckedChangeListener((group, checkedId) -> {
            String theme;
            if (checkedId == binding.rbLight.getId()) theme = Constants.THEME_LIGHT;
            else if (checkedId == binding.rbDark.getId()) theme = Constants.THEME_DARK;
            else theme = Constants.THEME_SYSTEM;
            viewModel.setTheme(theme);
            applyTheme(theme);
        });

        binding.switchAppLock.setOnCheckedChangeListener((btn, checked) ->
            viewModel.setAppLockEnabled(checked));

        binding.switchBiometric.setOnCheckedChangeListener((btn, checked) ->
            viewModel.setBiometricEnabled(checked));

        binding.rgCurrency.setOnCheckedChangeListener((group, checkedId) -> {
            String currency = checkedId == binding.rbUsd.getId() ? Constants.CURRENCY_USD : Constants.CURRENCY_VND;
            viewModel.setCurrency(currency);
        });

        binding.btnSetPin.setOnClickListener(v -> showSetPinDialog());
        binding.toolbar.setNavigationOnClickListener(v ->
            androidx.navigation.Navigation.findNavController(v).navigateUp());
    }

    private void applyTheme(String theme) {
        switch (theme) {
            case Constants.THEME_LIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); break;
            case Constants.THEME_DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); break;
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }

    private void showSetPinDialog() {
        new PinSetupDialog().show(getChildFragmentManager(), "PinSetupDialog");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
