package com.example.moneymate.presentation.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymate.databinding.ItemTransactionBinding;
import com.example.moneymate.domain.model.Transaction;
import com.example.moneymate.domain.model.enums.TransactionType;
import com.example.moneymate.util.CurrencyFormatter;
import com.example.moneymate.util.DateUtils;

public class TransactionAdapter extends ListAdapter<Transaction, TransactionAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Transaction transaction);
    }

    private final OnItemClickListener listener;

    public TransactionAdapter(OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Transaction> DIFF_CALLBACK =
        new DiffUtil.ItemCallback<Transaction>() {
            @Override
            public boolean areItemsTheSame(@NonNull Transaction a, @NonNull Transaction b) {
                return a.getId() == b.getId();
            }
            @Override
            public boolean areContentsTheSame(@NonNull Transaction a, @NonNull Transaction b) {
                return a.getAmount() == b.getAmount() && a.getDate() == b.getDate()
                    && a.getType() == b.getType();
            }
        };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTransactionBinding binding = ItemTransactionBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemTransactionBinding binding;

        ViewHolder(ItemTransactionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Transaction transaction, OnItemClickListener listener) {
            binding.tvDate.setText(DateUtils.formatDate(transaction.getDate()));
            binding.tvNote.setText(transaction.getNote() != null ? transaction.getNote() : "");

            String amount = CurrencyFormatter.formatVnd(transaction.getAmount());
            if (transaction.getType() == TransactionType.INCOME) {
                binding.tvAmount.setText("+" + amount);
                binding.tvAmount.setTextColor(0xFF4CAF50);
            } else if (transaction.getType() == TransactionType.EXPENSE) {
                binding.tvAmount.setText("-" + amount);
                binding.tvAmount.setTextColor(0xFFF44336);
            } else {
                binding.tvAmount.setText(amount);
                binding.tvAmount.setTextColor(0xFF2196F3);
            }

            binding.getRoot().setOnClickListener(v -> listener.onItemClick(transaction));
        }
    }
}
