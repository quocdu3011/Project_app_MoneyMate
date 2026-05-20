package com.example.moneymate.presentation.screen.budget;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.moneymate.databinding.DialogAddEditBudgetBinding;
import com.example.moneymate.domain.model.Budget;
import com.example.moneymate.domain.model.Category;
import com.example.moneymate.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddEditBudgetDialog extends DialogFragment {

    private DialogAddEditBudgetBinding binding;
    private BudgetViewModel viewModel;
    private List<Category> categoryList = new ArrayList<>();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DialogAddEditBudgetBinding.inflate(LayoutInflater.from(requireContext()));
        viewModel = new ViewModelProvider(requireParentFragment()).get(BudgetViewModel.class);

        int month = viewModel.getCurrentMonth();
        int year = viewModel.getCurrentYear();
        binding.tvBudgetMonth.setText(DateUtils.formatMonthYear(month, year));

        viewModel.getCategories().observe(this, categories -> {
            categoryList = categories;
            List<String> names = new ArrayList<>();
            for (Category c : categories) names.add(c.getName());
            binding.spinnerBudgetCategory.setAdapter(new ArrayAdapter<>(
                    requireContext(), android.R.layout.simple_spinner_item, names));
        });

        binding.btnSaveBudget.setOnClickListener(v -> saveBudget());
        binding.btnCancel.setOnClickListener(v -> dismiss());

        viewModel.getSaveSuccess().observe(this, success -> {
            if (Boolean.TRUE.equals(success)) {
                Toast.makeText(requireContext(), "Đã lưu ngân sách", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        return new AlertDialog.Builder(requireContext())
                .setView(binding.getRoot())
                .create();
    }

    private void saveBudget() {
        if (categoryList.isEmpty()) {
            Toast.makeText(requireContext(), "Chưa có danh mục", Toast.LENGTH_SHORT).show();
            return;
        }
        String amountStr = binding.etBudgetAmount.getText() != null
                ? binding.etBudgetAmount.getText().toString().trim() : "";
        if (amountStr.isEmpty()) {
            binding.etBudgetAmount.setError("Vui lòng nhập hạn mức");
            return;
        }
        double amount;
        try { amount = Double.parseDouble(amountStr); }
        catch (NumberFormatException e) {
            binding.etBudgetAmount.setError("Số tiền không hợp lệ");
            return;
        }

        int pos = binding.spinnerBudgetCategory.getSelectedItemPosition();
        Budget budget = new Budget();
        budget.setCategoryId(categoryList.get(pos).getId());
        budget.setAmount(amount);
        budget.setSpent(0);
        budget.setMonth(viewModel.getCurrentMonth());
        budget.setYear(viewModel.getCurrentYear());

        viewModel.saveBudget(budget);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
