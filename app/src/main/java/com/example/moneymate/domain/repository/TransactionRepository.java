package com.example.moneymate.domain.repository;

import com.example.moneymate.domain.model.Transaction;
import com.example.moneymate.domain.model.enums.TransactionType;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface TransactionRepository {
    Flowable<List<Transaction>> getAllTransactions();
    Flowable<List<Transaction>> getTransactionsByDateRange(long startDate, long endDate);
    Flowable<List<Transaction>> getTransactionsByAccount(long accountId);
    Flowable<List<Transaction>> getTransactionsByCategory(long categoryId);
    Flowable<List<Transaction>> getTransactionsByType(TransactionType type);
    Single<Transaction> getTransactionById(long id);
    Single<List<Transaction>> searchTransactions(String query);
    Single<Long> insert(Transaction transaction);
    Completable update(Transaction transaction);
    Completable delete(Transaction transaction);
    Single<Double> getTotalByTypeAndDateRange(TransactionType type, long startDate, long endDate);
}
