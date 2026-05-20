package com.example.moneymate.presentation.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymate.databinding.ItemCategoryBinding;
import com.example.moneymate.domain.model.Category;

public class CategoryAdapter extends ListAdapter<Category, CategoryAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Category category);
    }

    private final OnItemClickListener listener;

    public CategoryAdapter(OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Category> DIFF_CALLBACK =
        new DiffUtil.ItemCallback<Category>() {
            @Override
            public boolean areItemsTheSame(@NonNull Category a, @NonNull Category b) {
                return a.getId() == b.getId();
            }
            @Override
            public boolean areContentsTheSame(@NonNull Category a, @NonNull Category b) {
                return a.getName().equals(b.getName()) && a.getColor().equals(b.getColor());
            }
        };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryBinding binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemCategoryBinding binding;

        ViewHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Category category, OnItemClickListener listener) {
            binding.tvCategoryName.setText(category.getName());
            try {
                binding.viewColorDot.setBackgroundColor(
                    android.graphics.Color.parseColor(category.getColor()));
            } catch (Exception ignored) {}
            binding.getRoot().setOnClickListener(v -> listener.onItemClick(category));
        }
    }
}
