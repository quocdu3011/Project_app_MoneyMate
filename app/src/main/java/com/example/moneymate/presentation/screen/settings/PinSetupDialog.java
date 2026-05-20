package com.example.moneymate.presentation.screen.settings;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.moneymate.databinding.DialogPinSetupBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PinSetupDialog extends DialogFragment {

    private DialogPinSetupBinding binding;
    private SettingsViewModel viewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DialogPinSetupBinding.inflate(LayoutInflater.from(requireContext()));
        viewModel = new ViewModelProvider(requireParentFragment()).get(SettingsViewModel.class);

        binding.btnSavePin.setOnClickListener(v -> savePin());
        binding.btnCancelPin.setOnClickListener(v -> dismiss());

        viewModel.getPinSaved().observe(this, saved -> {
            if (Boolean.TRUE.equals(saved)) {
                Toast.makeText(requireContext(), "Đã lưu mã PIN", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        return new AlertDialog.Builder(requireContext())
                .setView(binding.getRoot())
                .create();
    }

    private void savePin() {
        String pin = binding.etPin.getText() != null
                ? binding.etPin.getText().toString().trim() : "";
        String confirm = binding.etPinConfirm.getText() != null
                ? binding.etPinConfirm.getText().toString().trim() : "";

        if (pin.length() < 4) {
            binding.etPin.setError("PIN phải có ít nhất 4 chữ số");
            return;
        }
        if (!pin.equals(confirm)) {
            binding.etPinConfirm.setError("Mã PIN không khớp");
            return;
        }

        viewModel.savePin(pin);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
