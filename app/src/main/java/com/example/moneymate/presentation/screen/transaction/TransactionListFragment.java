package com.example.moneymate.presentation.screen.transaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.moneymate.R;
import com.example.moneymate.databinding.FragmentTransactionListBinding;
import com.example.moneymate.domain.model.Category;
import com.example.moneymate.presentation.adapter.TransactionAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TransactionListFragment extends Fragment {
    private FragmentTransactionListBinding binding;
    private TransactionViewModel viewModel;
    private TransactionAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTransactionListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        adapter = new TransactionAdapter(transaction -> {
            Bundle bundle = new Bundle();
            bundle.putLong("transactionId", transaction.getId());
            Navigation.findNavController(view).navigate(R.id.action_transactions_to_detail, bundle);
        });

        binding.rvTransactions.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvTransactions.setAdapter(adapter);

        viewModel.getTransactions().observe(getViewLifecycleOwner(), transactions -> {
            adapter.submitList(transactions);
            binding.tvEmpty.setVisibility(transactions.isEmpty() ? View.VISIBLE : View.GONE);
        });

        // Truyền category map để hiện tên danh mục
        viewModel.getCategories().observe(getViewLifecycleOwner(), this::updateCategoryMap);

        binding.fabAdd.setOnClickListener(v ->
            Navigation.findNavController(v).navigate(R.id.action_transactions_to_add));
    }

    private void updateCategoryMap(List<Category> categories) {
        if (categories == null) return;
        Map<Long, String> map = new HashMap<>();
        for (Category c : categories) map.put(c.getId(), c.getName());
        adapter.setCategoryMap(map);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
