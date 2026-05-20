package com.example.moneymate.presentation.screen.account;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.moneymate.databinding.DialogAddEditAccountBinding;
import com.example.moneymate.domain.model.Account;
import com.example.moneymate.domain.model.enums.AccountType;

import java.util.Arrays;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddEditAccountDialog extends DialogFragment {

    private DialogAddEditAccountBinding binding;
    private AccountViewModel viewModel;

    private static final List<String> TYPE_LABELS = Arrays.asList(
            "Tiền mặt", "Ngân hàng", "Ví điện tử", "Thẻ tín dụng", "Tiết kiệm");
    private static final AccountType[] TYPES = {
            AccountType.CASH, AccountType.BANK, AccountType.E_WALLET,
            AccountType.CREDIT_CARD, AccountType.SAVINGS};
    private static final String[] COLORS = {
            "#4CAF50", "#2196F3", "#9C27B0", "#F44336", "#FF9800"};

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DialogAddEditAccountBinding.inflate(LayoutInflater.from(requireContext()));
        viewModel = new ViewModelProvider(requireParentFragment()).get(AccountViewModel.class);

        binding.spinnerAccountType.setAdapter(new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_item, TYPE_LABELS));

        binding.btnSaveAccount.setOnClickListener(v -> saveAccount());
        binding.btnCancel.setOnClickListener(v -> dismiss());

        viewModel.getSaveSuccess().observe(this, success -> {
            if (Boolean.TRUE.equals(success)) {
                Toast.makeText(requireContext(), "Đã lưu tài khoản", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        return new AlertDialog.Builder(requireContext())
                .setView(binding.getRoot())
                .create();
    }

    private void saveAccount() {
        String name = binding.etAccountName.getText() != null
                ? binding.etAccountName.getText().toString().trim() : "";
        if (name.isEmpty()) {
            binding.etAccountName.setError("Vui lòng nhập tên tài khoản");
            return;
        }

        double balance = 0;
        String balanceStr = binding.etAccountBalance.getText() != null
                ? binding.etAccountBalance.getText().toString().trim() : "";
        if (!balanceStr.isEmpty()) {
            try { balance = Double.parseDouble(balanceStr); } catch (NumberFormatException ignored) {}
        }

        int pos = binding.spinnerAccountType.getSelectedItemPosition();
        Account account = new Account();
        account.setName(name);
        account.setType(TYPES[pos]);
        account.setBalance(balance);
        account.setColor(COLORS[pos]);
        account.setIcon("ic_account");
        account.setDefault(false);

        viewModel.saveAccount(account);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
