package com.example.moneymate.presentation.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymate.databinding.ItemAccountBinding;
import com.example.moneymate.domain.model.Account;
import com.example.moneymate.util.CurrencyFormatter;

public class AccountAdapter extends ListAdapter<Account, AccountAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Account account);
    }

    private final OnItemClickListener listener;

    public AccountAdapter(OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Account> DIFF_CALLBACK =
        new DiffUtil.ItemCallback<Account>() {
            @Override
            public boolean areItemsTheSame(@NonNull Account a, @NonNull Account b) {
                return a.getId() == b.getId();
            }
            @Override
            public boolean areContentsTheSame(@NonNull Account a, @NonNull Account b) {
                return a.getBalance() == b.getBalance() && a.getName().equals(b.getName());
            }
        };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAccountBinding binding = ItemAccountBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemAccountBinding binding;

        ViewHolder(ItemAccountBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Account account, OnItemClickListener listener) {
            binding.tvAccountName.setText(account.getName());
            binding.tvAccountBalance.setText(CurrencyFormatter.formatVnd(account.getBalance()));
            binding.tvAccountType.setText(account.getType().name());
            binding.getRoot().setOnClickListener(v -> listener.onItemClick(account));
        }
    }
}
