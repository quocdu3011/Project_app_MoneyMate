package com.example.moneymate.domain.repository;

import com.example.moneymate.domain.model.Budget;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface BudgetRepository {
    Flowable<List<Budget>> getBudgetsByMonthYear(int month, int year);
    Single<Budget> getBudgetByCategoryAndMonthYear(long categoryId, int month, int year);
    Single<Long> insert(Budget budget);
    Completable update(Budget budget);
    Completable delete(Budget budget);
    Completable updateSpent(long categoryId, int month, int year, double spent);
}
