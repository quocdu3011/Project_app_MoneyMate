package com.example.moneymate.presentation.screen.home;

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
import com.example.moneymate.databinding.FragmentHomeBinding;
import com.example.moneymate.presentation.adapter.TransactionAdapter;
import com.example.moneymate.util.CurrencyFormatter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private TransactionAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        setupRecyclerView();
        observeViewModel();

        binding.fabAddTransaction.setOnClickListener(v ->
            Navigation.findNavController(v).navigate(R.id.action_home_to_addTransaction));
    }

    private void setupRecyclerView() {
        adapter = new TransactionAdapter(transaction ->
            Navigation.findNavController(requireView())
                .navigate(R.id.action_home_to_transactionDetail,
                    createBundle(transaction.getId())));
        binding.rvRecentTransactions.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvRecentTransactions.setAdapter(adapter);
    }

    private void observeViewModel() {
        viewModel.getTotalBalance().observe(getViewLifecycleOwner(), balance ->
            binding.tvTotalBalance.setText(CurrencyFormatter.formatVnd(balance)));

        viewModel.getMonthlyIncome().observe(getViewLifecycleOwner(), income ->
            binding.tvMonthlyIncome.setText(CurrencyFormatter.formatVnd(income)));

        viewModel.getMonthlyExpense().observe(getViewLifecycleOwner(), expense ->
            binding.tvMonthlyExpense.setText(CurrencyFormatter.formatVnd(expense)));

        viewModel.getRecentTransactions().observe(getViewLifecycleOwner(), transactions -> {
            adapter.submitList(transactions);
            binding.tvEmptyState.setVisibility(transactions.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }

    private Bundle createBundle(long id) {
        Bundle bundle = new Bundle();
        bundle.putLong("transactionId", id);
        return bundle;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
