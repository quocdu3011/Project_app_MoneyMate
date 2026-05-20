package com.example.moneymate.data.repository;

import com.example.moneymate.data.local.database.dao.BudgetDao;
import com.example.moneymate.data.mapper.BudgetMapper;
import com.example.moneymate.domain.model.Budget;
import com.example.moneymate.domain.repository.BudgetRepository;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class BudgetRepositoryImpl implements BudgetRepository {
    private final BudgetDao budgetDao;

    @Inject
    public BudgetRepositoryImpl(BudgetDao budgetDao) {
        this.budgetDao = budgetDao;
    }

    @Override
    public Flowable<List<Budget>> getBudgetsByMonthYear(int month, int year) {
        return budgetDao.getBudgetsByMonthYear(month, year)
                .map(entities -> entities.stream().map(BudgetMapper::toDomain).collect(Collectors.toList()));
    }

    @Override
    public Single<Budget> getBudgetByCategoryAndMonthYear(long categoryId, int month, int year) {
        return budgetDao.getBudgetByCategoryAndMonthYear(categoryId, month, year)
                .map(BudgetMapper::toDomain);
    }

    @Override
    public Single<Long> insert(Budget budget) {
        return budgetDao.insert(BudgetMapper.toEntity(budget));
    }

    @Override
    public Completable update(Budget budget) {
        return budgetDao.update(BudgetMapper.toEntity(budget));
    }

    @Override
    public Completable delete(Budget budget) {
        return budgetDao.delete(BudgetMapper.toEntity(budget));
    }

    @Override
    public Completable updateSpent(long categoryId, int month, int year, double spent) {
        return budgetDao.updateSpent(categoryId, month, year, spent);
    }
}
