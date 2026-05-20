package com.example.moneymate.presentation.screen.transaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.moneymate.databinding.FragmentTransactionDetailBinding;
import com.example.moneymate.domain.model.Transaction;
import com.example.moneymate.domain.model.enums.TransactionType;
import com.example.moneymate.util.CurrencyFormatter;
import com.example.moneymate.util.DateUtils;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TransactionDetailFragment extends Fragment {
    private FragmentTransactionDetailBinding binding;
    private TransactionViewModel viewModel;
    private long transactionId = -1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTransactionDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        if (getArguments() != null) {
            transactionId = getArguments().getLong("transactionId", -1);
        }

        binding.toolbar.setNavigationOnClickListener(v ->
                Navigation.findNavController(v).navigateUp());

        if (transactionId != -1) {
            viewModel.loadTransaction(transactionId);
        }

        viewModel.getSelectedTransaction().observe(getViewLifecycleOwner(), this::bindTransaction);

        binding.btnDeleteTransaction.setOnClickListener(v -> confirmDelete());
    }

    private void bindTransaction(Transaction transaction) {
        if (transaction == null) return;

        String amount = CurrencyFormatter.formatVnd(transaction.getAmount());
        if (transaction.getType() == TransactionType.INCOME) {
            binding.tvDetailAmount.setText("+" + amount);
            binding.tvDetailAmount.setTextColor(0xFF4CAF50);
            binding.tvDetailType.setText("THU NHẬP");
        } else if (transaction.getType() == TransactionType.EXPENSE) {
            binding.tvDetailAmount.setText("-" + amount);
            binding.tvDetailAmount.setTextColor(0xFFF44336);
            binding.tvDetailType.setText("CHI TIÊU");
        } else {
            binding.tvDetailAmount.setText(amount);
            binding.tvDetailAmount.setTextColor(0xFF2196F3);
            binding.tvDetailType.setText("CHUYỂN KHOẢN");
        }

        binding.tvDetailDate.setText(DateUtils.formatDate(transaction.getDate()));
        binding.tvDetailNote.setText(transaction.getNote() != null ? transaction.getNote() : "");

        // Load category and account names
        viewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories == null) return;
            categories.stream()
                    .filter(c -> c.getId() == transaction.getCategoryId())
                    .findFirst()
                    .ifPresent(c -> binding.tvDetailCategory.setText(c.getName()));
        });

        viewModel.getAccounts().observe(getViewLifecycleOwner(), accounts -> {
            if (accounts == null) return;
            accounts.stream()
                    .filter(a -> a.getId() == transaction.getAccountId())
                    .findFirst()
                    .ifPresent(a -> binding.tvDetailAccount.setText(a.getName()));
        });
    }

    private void confirmDelete() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xóa giao dịch")
                .setMessage("Bạn có chắc muốn xóa giao dịch này không?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteTransaction())
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteTransaction() {
        Transaction transaction = viewModel.getSelectedTransaction().getValue();
        if (transaction != null) {
            viewModel.deleteTransaction(transaction);
            Toast.makeText(requireContext(), "Đã xóa giao dịch", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(requireView()).navigateUp();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
