package com.example.moneymate.presentation.screen.transaction;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.moneymate.R;
import com.example.moneymate.databinding.FragmentAddTransactionBinding;
import com.example.moneymate.domain.model.Account;
import com.example.moneymate.domain.model.Category;
import com.example.moneymate.domain.model.Transaction;
import com.example.moneymate.domain.model.enums.TransactionType;
import com.example.moneymate.util.DateUtils;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddTransactionFragment extends Fragment {
    private FragmentAddTransactionBinding binding;
    private TransactionViewModel viewModel;
    private TransactionType selectedType = TransactionType.EXPENSE;
    private long selectedDate = System.currentTimeMillis();
    private List<Account> accountList = new ArrayList<>();
    private List<Category> categoryList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddTransactionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        setupTabs();
        setupDatePicker();
        observeViewModel();

        binding.btnSave.setOnClickListener(v -> saveTransaction());
        binding.toolbar.setNavigationOnClickListener(v ->
            Navigation.findNavController(v).navigateUp());
    }

    private void setupTabs() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Chi tiêu"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Thu nhập"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Chuyển khoản"));

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0: selectedType = TransactionType.EXPENSE; break;
                    case 1: selectedType = TransactionType.INCOME; break;
                    case 2: selectedType = TransactionType.TRANSFER; break;
                }
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupDatePicker() {
        binding.tvDate.setText(DateUtils.formatDate(selectedDate));
        binding.tvDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(requireContext(), (dp, y, m, d) -> {
                cal.set(y, m, d);
                selectedDate = cal.getTimeInMillis();
                binding.tvDate.setText(DateUtils.formatDate(selectedDate));
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void observeViewModel() {
        viewModel.getAccounts().observe(getViewLifecycleOwner(), accounts -> {
            accountList = accounts;
            List<String> names = new ArrayList<>();
            for (Account a : accounts) names.add(a.getName());
            binding.spinnerAccount.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, names));
        });

        viewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            categoryList = categories;
            List<String> names = new ArrayList<>();
            for (Category c : categories) names.add(c.getName());
            binding.spinnerCategory.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, names));
        });

        viewModel.getSaveSuccess().observe(getViewLifecycleOwner(), success -> {
            if (Boolean.TRUE.equals(success)) {
                Toast.makeText(requireContext(), "Đã lưu giao dịch", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireView()).navigateUp();
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), err -> {
            if (err != null) Toast.makeText(requireContext(), err, Toast.LENGTH_SHORT).show();
        });
    }

    private void saveTransaction() {
        String amountStr = binding.etAmount.getText().toString().trim();
        if (amountStr.isEmpty()) {
            binding.etAmount.setError("Vui lòng nhập số tiền");
            return;
        }
        double amount = Double.parseDouble(amountStr);
        if (amount <= 0) {
            binding.etAmount.setError("Số tiền phải lớn hơn 0");
            return;
        }
        if (accountList.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng thêm tài khoản trước", Toast.LENGTH_SHORT).show();
            return;
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setType(selectedType);
        transaction.setNote(binding.etNote.getText().toString().trim());
        transaction.setDate(selectedDate);
        transaction.setAccountId(accountList.get(binding.spinnerAccount.getSelectedItemPosition()).getId());
        if (!categoryList.isEmpty()) {
            transaction.setCategoryId(categoryList.get(binding.spinnerCategory.getSelectedItemPosition()).getId());
        }
        viewModel.saveTransaction(transaction);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
