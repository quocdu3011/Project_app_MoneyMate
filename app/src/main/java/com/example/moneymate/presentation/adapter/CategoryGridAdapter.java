package com.example.moneymate.presentation.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymate.R;
import com.example.moneymate.databinding.ItemCategoryGridBinding;
import com.example.moneymate.domain.model.Category;

import java.util.HashMap;
import java.util.Map;

public class CategoryGridAdapter extends ListAdapter<Category, CategoryGridAdapter.ViewHolder> {

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    private final OnCategoryClickListener listener;
    private long selectedCategoryId = -1;

    private static final Map<String, String> ICON_MAP = new HashMap<>();
    static {
        ICON_MAP.put("ic_food", "🍜");
        ICON_MAP.put("ic_transport", "🚗");
        ICON_MAP.put("ic_shopping", "🛒");
        ICON_MAP.put("ic_home", "🏠");
        ICON_MAP.put("ic_bill", "⚡");
        ICON_MAP.put("ic_entertainment", "🎮");
        ICON_MAP.put("ic_education", "📚");
        ICON_MAP.put("ic_health", "🏥");
        ICON_MAP.put("ic_clothes", "👔");
        ICON_MAP.put("ic_travel", "✈️");
        ICON_MAP.put("ic_pet", "🐕");
        ICON_MAP.put("ic_beauty", "💄");
        ICON_MAP.put("ic_other", "🔧");
        ICON_MAP.put("ic_salary", "💰");
        ICON_MAP.put("ic_bonus", "💵");
        ICON_MAP.put("ic_invest", "📈");
        ICON_MAP.put("ic_business", "🏪");
        ICON_MAP.put("ic_gift", "🎁");
        ICON_MAP.put("ic_category", "📂");
    }

    public CategoryGridAdapter(OnCategoryClickListener listener) {
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
                return a.getName().equals(b.getName());
            }
        };

    public void setSelectedCategory(long id) {
        this.selectedCategoryId = id;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryGridBinding binding = ItemCategoryGridBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), listener, selectedCategoryId);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemCategoryGridBinding binding;

        ViewHolder(ItemCategoryGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Category category, OnCategoryClickListener listener, long selectedId) {
            binding.tvCategoryName.setText(category.getName());
            String emoji = ICON_MAP.containsKey(category.getIcon())
                    ? ICON_MAP.get(category.getIcon()) : "📂";
            binding.tvCategoryIcon.setText(emoji);

            boolean isSelected = category.getId() == selectedId;
            binding.flIconBg.setBackgroundResource(isSelected
                ? R.drawable.bg_category_circle_selected
                : R.drawable.bg_category_circle);

            binding.getRoot().setOnClickListener(v -> listener.onCategoryClick(category));
        }
    }
}
