package com.example.moneymate.presentation.screen.category;

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

import com.example.moneymate.databinding.DialogAddEditCategoryBinding;
import com.example.moneymate.domain.model.Category;
import com.example.moneymate.domain.model.enums.TransactionType;

import java.util.Arrays;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddEditCategoryDialog extends DialogFragment {

    private DialogAddEditCategoryBinding binding;
    private CategoryViewModel viewModel;

    private static final List<String> COLOR_LABELS = Arrays.asList(
            "Đỏ", "Cam", "Vàng", "Xanh lá", "Xanh dương", "Tím", "Hồng", "Nâu", "Xám");
    private static final String[] COLOR_VALUES = {
            "#F44336", "#FF9800", "#FFEB3B", "#4CAF50", "#2196F3",
            "#9C27B0", "#E91E63", "#795548", "#9E9E9E"};

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DialogAddEditCategoryBinding.inflate(LayoutInflater.from(requireContext()));
        viewModel = new ViewModelProvider(requireParentFragment()).get(CategoryViewModel.class);

        binding.spinnerCategoryColor.setAdapter(new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_item, COLOR_LABELS));

        binding.btnSaveCategory.setOnClickListener(v -> saveCategory());
        binding.btnCancel.setOnClickListener(v -> dismiss());

        viewModel.getSaveSuccess().observe(this, success -> {
            if (Boolean.TRUE.equals(success)) {
                Toast.makeText(requireContext(), "Đã lưu danh mục", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        return new AlertDialog.Builder(requireContext())
                .setView(binding.getRoot())
                .create();
    }

    private void saveCategory() {
        String name = binding.etCategoryName.getText() != null
                ? binding.etCategoryName.getText().toString().trim() : "";
        if (name.isEmpty()) {
            binding.etCategoryName.setError("Vui lòng nhập tên danh mục");
            return;
        }

        TransactionType type = binding.rbIncome.isChecked()
                ? TransactionType.INCOME : TransactionType.EXPENSE;
        int colorPos = binding.spinnerCategoryColor.getSelectedItemPosition();

        Category category = new Category();
        category.setName(name);
        category.setType(type);
        category.setColor(COLOR_VALUES[colorPos]);
        category.setIcon("ic_category");

        viewModel.saveCategory(category);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
