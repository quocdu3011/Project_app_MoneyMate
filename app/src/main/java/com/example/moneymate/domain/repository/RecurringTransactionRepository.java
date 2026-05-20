package com.example.moneymate.domain.repository;

import com.example.moneymate.domain.model.RecurringTransaction;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface RecurringTransactionRepository {
    Flowable<List<RecurringTransaction>> getAllActive();
    Single<List<RecurringTransaction>> getDueTransactions(long today);
    Single<Long> insert(RecurringTransaction recurring);
    Completable update(RecurringTransaction recurring);
    Completable delete(RecurringTransaction recurring);
}
