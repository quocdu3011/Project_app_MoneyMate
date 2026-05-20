package com.example.moneymate.presentation.screen.report;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.moneymate.databinding.FragmentReportBinding;
import com.example.moneymate.domain.model.Category;
import com.example.moneymate.util.CurrencyFormatter;
import com.example.moneymate.util.DateUtils;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ReportFragment extends Fragment {
    private FragmentReportBinding binding;
    private ReportViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentReportBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ReportViewModel.class);

        setupPieChart();
        setupMonthNavigation();
        observeViewModel();
    }

    private void setupPieChart() {
        binding.pieChart.setUsePercentValues(true);
        binding.pieChart.getDescription().setEnabled(false);
        binding.pieChart.setDrawHoleEnabled(true);
        binding.pieChart.setHoleRadius(40f);
        binding.pieChart.setTransparentCircleRadius(45f);
        binding.pieChart.getLegend().setEnabled(true);
    }

    private void setupMonthNavigation() {
        updateMonthLabel();
        binding.btnPrevMonth.setOnClickListener(v -> {
            int m = viewModel.getCurrentMonth(), y = viewModel.getCurrentYear();
            if (m == 1) { m = 12; y--; } else m--;
            viewModel.setMonthYear(m, y);
            updateMonthLabel();
        });
        binding.btnNextMonth.setOnClickListener(v -> {
            int m = viewModel.getCurrentMonth(), y = viewModel.getCurrentYear();
            if (m == 12) { m = 1; y++; } else m++;
            viewModel.setMonthYear(m, y);
            updateMonthLabel();
        });
    }

    private void updateMonthLabel() {
        binding.tvMonthYear.setText(DateUtils.formatMonthYear(
            viewModel.getCurrentMonth(), viewModel.getCurrentYear()));
    }

    private void observeViewModel() {
        viewModel.getTotalIncome().observe(getViewLifecycleOwner(), income ->
            binding.tvTotalIncome.setText(CurrencyFormatter.formatVnd(income)));

        viewModel.getTotalExpense().observe(getViewLifecycleOwner(), expense ->
            binding.tvTotalExpense.setText(CurrencyFormatter.formatVnd(expense)));

        viewModel.getExpenseByCategory().observe(getViewLifecycleOwner(), expenseMap -> {
            List<Category> cats = viewModel.getCategories().getValue();
            updatePieChart(expenseMap, cats);
        });
    }

    private void updatePieChart(Map<Long, Double> expenseMap, List<Category> categories) {
        if (expenseMap == null || expenseMap.isEmpty()) {
            binding.pieChart.setVisibility(View.GONE);
            return;
        }
        binding.pieChart.setVisibility(View.VISIBLE);
        List<PieEntry> entries = new ArrayList<>();
        int[] colors = {Color.parseColor("#FF5722"), Color.parseColor("#2196F3"),
            Color.parseColor("#9C27B0"), Color.parseColor("#4CAF50"),
            Color.parseColor("#FF9800"), Color.parseColor("#E91E63")};

        int i = 0;
        for (Map.Entry<Long, Double> entry : expenseMap.entrySet()) {
            String label = getCategoryName(entry.getKey(), categories);
            entries.add(new PieEntry(entry.getValue().floatValue(), label));
            i++;
        }

        PieDataSet dataSet = new PieDataSet(entries, "Chi tiêu");
        dataSet.setColors(colors);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.WHITE);

        binding.pieChart.setData(new PieData(dataSet));
        binding.pieChart.invalidate();
    }

    private String getCategoryName(long categoryId, List<Category> categories) {
        if (categories == null) return "Khác";
        for (Category c : categories) {
            if (c.getId() == categoryId) return c.getName();
        }
        return "Khác";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
