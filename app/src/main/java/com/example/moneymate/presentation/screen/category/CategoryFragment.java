package com.example.moneymate.presentation.screen.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymate.databinding.FragmentCategoryBinding;
import com.example.moneymate.domain.model.Category;
import com.example.moneymate.domain.model.enums.TransactionType;
import com.example.moneymate.presentation.adapter.CategoryAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.stream.Collectors;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CategoryFragment extends Fragment {
    private FragmentCategoryBinding binding;
    private CategoryViewModel viewModel;
    private CategoryAdapter adapter;
    private TransactionType currentType = TransactionType.EXPENSE;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        adapter = new CategoryAdapter(category -> {});
        binding.rvCategories.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvCategories.setAdapter(adapter);

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Chi tiêu"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Thu nhập"));
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                currentType = tab.getPosition() == 0 ? TransactionType.EXPENSE : TransactionType.INCOME;
                filterCategories();
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        viewModel.getCategories().observe(getViewLifecycleOwner(), categories -> filterCategories());

        binding.fabAddCategory.setOnClickListener(v -> showAddCategoryDialog());
    }

    private void filterCategories() {
        List<Category> all = viewModel.getCategories().getValue();
        if (all == null) return;
        List<Category> filtered = all.stream()
            .filter(c -> c.getType() == currentType)
            .collect(Collectors.toList());
        adapter.submitList(filtered);
    }

    private void showAddCategoryDialog() {
        new AddEditCategoryDialog().show(getChildFragmentManager(), "AddEditCategoryDialog");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
