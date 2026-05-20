package com.example.moneymate.presentation.adapter;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymate.R;
import com.example.moneymate.databinding.ItemBudgetBinding;
import com.example.moneymate.domain.model.Budget;
import com.example.moneymate.util.CurrencyFormatter;

import java.util.HashMap;
import java.util.Map;

public class BudgetAdapter extends ListAdapter<Budget, BudgetAdapter.ViewHolder> {

    private Map<Long, String> categoryMap = new HashMap<>();

    public BudgetAdapter() {
        super(DIFF_CALLBACK);
    }

    public void setCategoryMap(Map<Long, String> map) {
        this.categoryMap = map != null ? map : new HashMap<>();
        notifyDataSetChanged();
    }

    private static final DiffUtil.ItemCallback<Budget> DIFF_CALLBACK =
        new DiffUtil.ItemCallback<Budget>() {
            @Override
            public boolean areItemsTheSame(@NonNull Budget a, @NonNull Budget b) {
                return a.getId() == b.getId();
            }
            @Override
            public boolean areContentsTheSame(@NonNull Budget a, @NonNull Budget b) {
                return a.getSpent() == b.getSpent() && a.getAmount() == b.getAmount();
            }
        };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBudgetBinding binding = ItemBudgetBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), categoryMap);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemBudgetBinding binding;

        ViewHolder(ItemBudgetBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Budget budget, Map<Long, String> categoryMap) {
            String name = categoryMap.containsKey(budget.getCategoryId())
                ? categoryMap.get(budget.getCategoryId()) : "Danh mục";
            binding.tvBudgetCategory.setText(name);

            int percent = budget.getAmount() > 0
                ? (int) ((budget.getSpent() / budget.getAmount()) * 100) : 0;
            percent = Math.min(percent, 100);
            binding.tvBudgetPercent.setText(percent + "%");
            binding.progressBudget.setProgress(percent);

            int colorRes = percent >= 100 ? R.color.color_expense
                : percent >= 80 ? R.color.color_warning : R.color.color_primary;
            binding.progressBudget.setProgressTintList(
                ColorStateList.valueOf(ContextCompat.getColor(binding.getRoot().getContext(), colorRes)));

            binding.tvBudgetSpent.setText("Đã chi: " + CurrencyFormatter.formatVnd(budget.getSpent()));
            binding.tvBudgetTotal.setText("Hạn mức: " + CurrencyFormatter.formatVnd(budget.getAmount()));
        }
    }
}
