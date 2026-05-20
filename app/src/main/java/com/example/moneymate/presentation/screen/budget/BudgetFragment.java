package com.example.moneymate.presentation.screen.budget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.moneymate.databinding.FragmentBudgetBinding;
import com.example.moneymate.util.CurrencyFormatter;
import com.example.moneymate.util.DateUtils;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BudgetFragment extends Fragment {
    private FragmentBudgetBinding binding;
    private BudgetViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBudgetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(BudgetViewModel.class);

        updateMonthYearLabel();
        setupMonthNavigation();
        observeViewModel();

        binding.fabAddBudget.setOnClickListener(v -> showAddBudgetDialog());
    }

    private void setupMonthNavigation() {
        binding.btnPrevMonth.setOnClickListener(v -> {
            int month = viewModel.getCurrentMonth();
            int year = viewModel.getCurrentYear();
            if (month == 1) { month = 12; year--; } else { month--; }
            viewModel.setMonthYear(month, year);
            updateMonthYearLabel();
        });

        binding.btnNextMonth.setOnClickListener(v -> {
            int month = viewModel.getCurrentMonth();
            int year = viewModel.getCurrentYear();
            if (month == 12) { month = 1; year++; } else { month++; }
            viewModel.setMonthYear(month, year);
            updateMonthYearLabel();
        });
    }

    private void updateMonthYearLabel() {
        binding.tvMonthYear.setText(DateUtils.formatMonthYear(
            viewModel.getCurrentMonth(), viewModel.getCurrentYear()));
    }

    private void observeViewModel() {
        viewModel.getBudgets().observe(getViewLifecycleOwner(), budgets -> {
            double totalBudget = budgets.stream().mapToDouble(b -> b.getAmount()).sum();
            double totalSpent = budgets.stream().mapToDouble(b -> b.getSpent()).sum();
            binding.tvTotalBudget.setText(CurrencyFormatter.formatVnd(totalBudget));
            binding.tvTotalSpent.setText(CurrencyFormatter.formatVnd(totalSpent));
            int progress = totalBudget > 0 ? (int) ((totalSpent / totalBudget) * 100) : 0;
            binding.progressTotal.setProgress(Math.min(progress, 100));
        });
    }

    private void showAddBudgetDialog() {
        new AddEditBudgetDialog().show(getChildFragmentManager(), "AddEditBudgetDialog");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
