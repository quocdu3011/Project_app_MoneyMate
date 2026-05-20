package com.example.moneymate.presentation.screen.report;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.moneymate.R;
import com.example.moneymate.databinding.FragmentReportBinding;
import com.example.moneymate.domain.model.Category;
import com.example.moneymate.util.CurrencyFormatter;
import com.example.moneymate.util.DateUtils;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ReportFragment extends Fragment {
    private FragmentReportBinding binding;
    private ReportViewModel viewModel;

    private static final int[] CHART_COLORS = {
        Color.parseColor("#FF5722"), Color.parseColor("#2196F3"),
        Color.parseColor("#9C27B0"), Color.parseColor("#4CAF50"),
        Color.parseColor("#FF9800"), Color.parseColor("#E91E63"),
        Color.parseColor("#00BCD4"), Color.parseColor("#795548")
    };

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
        setupBarChart();
        setupMonthNavigation();
        observeViewModel();
    }

    private void setupPieChart() {
        int textColor = ContextCompat.getColor(requireContext(), R.color.color_on_surface);
        binding.pieChart.setUsePercentValues(true);
        binding.pieChart.getDescription().setEnabled(false);
        binding.pieChart.setDrawHoleEnabled(true);
        binding.pieChart.setHoleRadius(40f);
        binding.pieChart.setTransparentCircleRadius(45f);
        binding.pieChart.getLegend().setEnabled(true);
        binding.pieChart.getLegend().setTextColor(textColor);
        binding.pieChart.getLegend().setTextSize(12f);
        binding.pieChart.setEntryLabelColor(textColor);
        binding.pieChart.setEntryLabelTextSize(11f);
    }

    private void setupBarChart() {
        int textColor = ContextCompat.getColor(requireContext(), R.color.color_on_surface);
        binding.barChart.getDescription().setEnabled(false);
        binding.barChart.getLegend().setEnabled(false);
        binding.barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        binding.barChart.getXAxis().setTextColor(textColor);
        binding.barChart.getXAxis().setGranularity(1f);
        binding.barChart.getAxisLeft().setTextColor(textColor);
        binding.barChart.getAxisRight().setEnabled(false);
        binding.barChart.setFitBars(true);
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

        viewModel.getExpenseByCategory().observe(getViewLifecycleOwner(), expenseMap ->
            updateCharts(expenseMap, viewModel.getCategories().getValue()));

        // Cập nhật chart khi categories load xong
        viewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            Map<Long, Double> expenseMap = viewModel.getExpenseByCategory().getValue();
            if (expenseMap != null) updateCharts(expenseMap, categories);
        });
    }

    private void updateCharts(Map<Long, Double> expenseMap, List<Category> categories) {
        updatePieChart(expenseMap, categories);
        updateBarChart(expenseMap, categories);
    }

    private void updatePieChart(Map<Long, Double> expenseMap, List<Category> categories) {
        if (expenseMap == null || expenseMap.isEmpty()) {
            binding.pieChart.setVisibility(View.GONE);
            return;
        }
        binding.pieChart.setVisibility(View.VISIBLE);
        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<Long, Double> entry : expenseMap.entrySet()) {
            entries.add(new PieEntry(entry.getValue().floatValue(),
                getCategoryName(entry.getKey(), categories)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(CHART_COLORS);
        dataSet.setValueTextSize(11f);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setSliceSpace(2f);

        int textColor = ContextCompat.getColor(requireContext(), R.color.color_on_surface);
        binding.pieChart.getLegend().setTextColor(textColor);
        binding.pieChart.setEntryLabelColor(textColor);
        binding.pieChart.setData(new PieData(dataSet));
        binding.pieChart.animateY(600);
        binding.pieChart.invalidate();
    }

    private void updateBarChart(Map<Long, Double> expenseMap, List<Category> categories) {
        if (expenseMap == null || expenseMap.isEmpty()) {
            binding.barChart.setVisibility(View.GONE);
            return;
        }
        binding.barChart.setVisibility(View.VISIBLE);

        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        int i = 0;
        for (Map.Entry<Long, Double> entry : expenseMap.entrySet()) {
            entries.add(new BarEntry(i, entry.getValue().floatValue()));
            labels.add(getCategoryName(entry.getKey(), categories));
            i++;
        }

        int textColor = ContextCompat.getColor(requireContext(), R.color.color_on_surface);
        BarDataSet dataSet = new BarDataSet(entries, "Chi tiêu");
        dataSet.setColors(CHART_COLORS);
        dataSet.setValueTextSize(10f);
        dataSet.setValueTextColor(textColor);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.7f);

        binding.barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        binding.barChart.getXAxis().setLabelCount(labels.size());
        binding.barChart.getXAxis().setTextColor(textColor);
        binding.barChart.getAxisLeft().setTextColor(textColor);
        binding.barChart.setData(barData);
        binding.barChart.animateY(600);
        binding.barChart.invalidate();
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
