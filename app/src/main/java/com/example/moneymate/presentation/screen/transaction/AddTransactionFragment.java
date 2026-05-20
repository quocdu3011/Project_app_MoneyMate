package com.example.moneymate.presentation.screen.transaction;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.moneymate.R;
import com.example.moneymate.databinding.FragmentAddTransactionBinding;
import com.example.moneymate.domain.model.Account;
import com.example.moneymate.domain.model.Category;
import com.example.moneymate.domain.model.Transaction;
import com.example.moneymate.domain.model.enums.TransactionType;
import com.example.moneymate.presentation.adapter.CategoryGridAdapter;
import com.example.moneymate.util.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddTransactionFragment extends Fragment {

    private FragmentAddTransactionBinding binding;
    private TransactionViewModel viewModel;
    private CategoryGridAdapter categoryAdapter;

    private TransactionType selectedType = TransactionType.EXPENSE;
    private Category selectedCategory = null;
    private List<Account> accountList = new ArrayList<>();
    private List<Category> allCategories = new ArrayList<>();
    private long selectedDate = System.currentTimeMillis();

    // Calculator state
    private String displayValue = "0";
    private double pendingValue = 0;
    private String pendingOperator = null;
    private boolean isNewInput = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddTransactionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        setupCategoryGrid();
        setupTabs();
        setupCalculator();
        observeViewModel();

        binding.btnCancel.setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp());
    }

    // ── Category grid ──────────────────────────────────────────────────────────

    private void setupCategoryGrid() {
        categoryAdapter = new CategoryGridAdapter(category -> {
            selectedCategory = category;
            categoryAdapter.setSelectedCategory(category.getId());
            showBottomPanel();
        });
        binding.rvCategories.setLayoutManager(new GridLayoutManager(requireContext(), 4));
        binding.rvCategories.setAdapter(categoryAdapter);
    }

    private void showBottomPanel() {
        if (binding.layoutBottomPanel.getVisibility() != View.VISIBLE) {
            binding.layoutBottomPanel.setVisibility(View.VISIBLE);
            binding.layoutBottomPanel.animate().alpha(1f).setDuration(200).start();
        }
    }

    // ── Tabs ───────────────────────────────────────────────────────────────────

    private void setupTabs() {
        binding.tabExpense.setOnClickListener(v -> selectTab(TransactionType.EXPENSE));
        binding.tabIncome.setOnClickListener(v -> selectTab(TransactionType.INCOME));
        binding.tabTransfer.setOnClickListener(v -> selectTab(TransactionType.TRANSFER));
        updateTabUI();
    }

    private void selectTab(TransactionType type) {
        selectedType = type;
        selectedCategory = null;
        if (categoryAdapter != null) categoryAdapter.setSelectedCategory(-1);
        resetCalculator();
        updateTabUI();
        filterCategories();
        // Transfer không cần chọn danh mục — hiện panel ngay
        if (type == TransactionType.TRANSFER) {
            binding.layoutBottomPanel.setVisibility(View.VISIBLE);
        } else {
            binding.layoutBottomPanel.setVisibility(View.GONE);
        }
    }

    private void updateTabUI() {
        binding.tabExpense.setBackgroundResource(selectedType == TransactionType.EXPENSE
                ? R.drawable.bg_tab_selected : R.drawable.bg_tab_unselected);
        binding.tabIncome.setBackgroundResource(selectedType == TransactionType.INCOME
                ? R.drawable.bg_tab_selected : R.drawable.bg_tab_unselected);
        binding.tabTransfer.setBackgroundResource(selectedType == TransactionType.TRANSFER
                ? R.drawable.bg_tab_selected : R.drawable.bg_tab_unselected);

        int activeColor = androidx.core.content.ContextCompat.getColor(
                requireContext(), R.color.color_on_surface);
        int inactiveColor = androidx.core.content.ContextCompat.getColor(
                requireContext(), R.color.color_on_surface_secondary);

        binding.tabExpense.setTextColor(selectedType == TransactionType.EXPENSE ? activeColor : inactiveColor);
        binding.tabIncome.setTextColor(selectedType == TransactionType.INCOME ? activeColor : inactiveColor);
        binding.tabTransfer.setTextColor(selectedType == TransactionType.TRANSFER ? activeColor : inactiveColor);
    }

    private void filterCategories() {
        if (allCategories.isEmpty()) return;
        List<Category> filtered;
        if (selectedType == TransactionType.TRANSFER) {
            filtered = new ArrayList<>();
        } else {
            filtered = allCategories.stream()
                    .filter(c -> c.getType() == selectedType)
                    .collect(Collectors.toList());
        }
        categoryAdapter.submitList(filtered);
    }

    // ── Calculator ─────────────────────────────────────────────────────────────

    private void setupCalculator() {
        View.OnClickListener digitListener = v -> {
            String digit = ((android.widget.Button) v).getText().toString();
            appendDigit(digit);
        };

        binding.btn0.setOnClickListener(digitListener);
        binding.btn1.setOnClickListener(digitListener);
        binding.btn2.setOnClickListener(digitListener);
        binding.btn3.setOnClickListener(digitListener);
        binding.btn4.setOnClickListener(digitListener);
        binding.btn5.setOnClickListener(digitListener);
        binding.btn6.setOnClickListener(digitListener);
        binding.btn7.setOnClickListener(digitListener);
        binding.btn8.setOnClickListener(digitListener);
        binding.btn9.setOnClickListener(digitListener);
        binding.btnDot.setOnClickListener(v -> appendDigit("."));

        binding.btnPlus.setOnClickListener(v -> pressOperator("+"));
        binding.btnMinus.setOnClickListener(v -> pressOperator("−"));

        binding.btnBackspace.setOnClickListener(v -> {
            if (displayValue.length() > 1) {
                displayValue = displayValue.substring(0, displayValue.length() - 1);
            } else {
                displayValue = "0";
            }
            updateDisplay();
        });

        binding.btnToday.setOnClickListener(v -> pickDate());
        binding.btnConfirm.setOnClickListener(v -> saveTransaction());
    }

    private void appendDigit(String digit) {
        if (isNewInput) {
            displayValue = digit.equals(".") ? "0." : digit;
            isNewInput = false;
        } else {
            if (digit.equals(".") && displayValue.contains(".")) return;
            if (displayValue.equals("0") && !digit.equals(".")) {
                displayValue = digit;
            } else {
                if (displayValue.length() >= 12) return;
                displayValue += digit;
            }
        }
        updateDisplay();
    }

    private void pressOperator(String op) {
        if (pendingOperator != null && !isNewInput) {
            // Chain: 100000 + 200000 → ấn + → tính 300000 trước
            double result = calculateResult();
            pendingValue = result;
            displayValue = formatNumber(result);
        } else {
            try { pendingValue = Double.parseDouble(displayValue); }
            catch (NumberFormatException e) { pendingValue = 0; }
        }
        pendingOperator = op;
        isNewInput = true;
        updateDisplay();
    }

    private double calculateResult() {
        if (pendingOperator != null && !isNewInput) {
            double current;
            try { current = Double.parseDouble(displayValue); }
            catch (NumberFormatException e) { current = 0; }
            double result = "+".equals(pendingOperator)
                    ? pendingValue + current
                    : pendingValue - current;
            return Math.max(result, 0);
        } else if (pendingOperator != null) {
            return Math.max(pendingValue, 0);
        } else {
            double current;
            try { current = Double.parseDouble(displayValue); }
            catch (NumberFormatException e) { current = 0; }
            return Math.max(current, 0);
        }
    }

    private void updateDisplay() {
        if (pendingOperator != null) {
            if (isNewInput) {
                // Vừa ấn toán tử: "100000 +"
                binding.tvAmountDisplay.setText(formatNumber(pendingValue) + " " + pendingOperator);
            } else {
                // Đang nhập số thứ 2: "100000 + 200000"
                binding.tvAmountDisplay.setText(
                        formatNumber(pendingValue) + " " + pendingOperator + " " + displayValue);
            }
        } else {
            binding.tvAmountDisplay.setText(displayValue);
        }
    }

    private void resetCalculator() {
        displayValue = "0";
        pendingValue = 0;
        pendingOperator = null;
        isNewInput = true;
        if (binding != null) binding.tvAmountDisplay.setText("0");
    }

    private String formatNumber(double v) {
        return v == (long) v ? String.valueOf((long) v) : String.valueOf(v);
    }

    private void pickDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(selectedDate);
        new DatePickerDialog(requireContext(), (dp, y, m, d) -> {
            cal.set(y, m, d);
            selectedDate = cal.getTimeInMillis();
            Toast.makeText(requireContext(),
                    "Ngày: " + DateUtils.formatDate(selectedDate), Toast.LENGTH_SHORT).show();
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    // ── Save ───────────────────────────────────────────────────────────────────

    private void saveTransaction() {
        double amount = calculateResult();
        if (amount <= 0) {
            Toast.makeText(requireContext(), "Vui lòng nhập số tiền", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedCategory == null && selectedType != TransactionType.TRANSFER) {
            Toast.makeText(requireContext(), "Vui lòng chọn danh mục", Toast.LENGTH_SHORT).show();
            return;
        }
        if (accountList.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng thêm tài khoản trước", Toast.LENGTH_SHORT).show();
            return;
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setType(selectedType);
        transaction.setNote(binding.etNote.getText() != null
                ? binding.etNote.getText().toString().trim() : "");
        transaction.setDate(selectedDate);
        transaction.setAccountId(accountList.get(0).getId());
        if (selectedCategory != null) {
            transaction.setCategoryId(selectedCategory.getId());
        }
        viewModel.saveTransaction(transaction);
    }

    // ── Observe ────────────────────────────────────────────────────────────────

    private void observeViewModel() {
        viewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            allCategories = categories != null ? categories : new ArrayList<>();
            filterCategories();
        });

        viewModel.getAccounts().observe(getViewLifecycleOwner(), accounts ->
                accountList = accounts != null ? accounts : new ArrayList<>());

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
