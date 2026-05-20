package com.example.moneymate.presentation.screen.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.moneymate.databinding.FragmentAccountBinding;
import com.example.moneymate.presentation.adapter.AccountAdapter;
import com.example.moneymate.util.CurrencyFormatter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AccountFragment extends Fragment {
    private FragmentAccountBinding binding;
    private AccountViewModel viewModel;
    private AccountAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        adapter = new AccountAdapter(account -> {});
        binding.rvAccounts.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvAccounts.setAdapter(adapter);

        viewModel.getAccounts().observe(getViewLifecycleOwner(), accounts ->
            adapter.submitList(accounts));

        viewModel.getTotalBalance().observe(getViewLifecycleOwner(), total ->
            binding.tvTotalAssets.setText(CurrencyFormatter.formatVnd(total)));

        binding.fabAddAccount.setOnClickListener(v -> showAddAccountDialog());
    }

    private void showAddAccountDialog() {
        new AddEditAccountDialog().show(getChildFragmentManager(), "AddEditAccountDialog");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
