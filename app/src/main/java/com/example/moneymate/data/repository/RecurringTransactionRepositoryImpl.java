package com.example.moneymate.data.repository;

import com.example.moneymate.data.local.database.dao.RecurringTransactionDao;
import com.example.moneymate.data.local.database.entity.RecurringTransactionEntity;
import com.example.moneymate.domain.model.RecurringTransaction;
import com.example.moneymate.domain.repository.RecurringTransactionRepository;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class RecurringTransactionRepositoryImpl implements RecurringTransactionRepository {
    private final RecurringTransactionDao dao;

    @Inject
    public RecurringTransactionRepositoryImpl(RecurringTransactionDao dao) {
        this.dao = dao;
    }

    private RecurringTransaction toDomain(RecurringTransactionEntity e) {
        return new RecurringTransaction(e.getId(), e.getTransactionId(), e.getFrequency(),
                e.getStartDate(), e.getEndDate(), e.getNextOccurrence(), e.isActive());
    }

    private RecurringTransactionEntity toEntity(RecurringTransaction d) {
        RecurringTransactionEntity e = new RecurringTransactionEntity();
        e.setId(d.getId());
        e.setTransactionId(d.getTransactionId());
        e.setFrequency(d.getFrequency());
        e.setStartDate(d.getStartDate());
        e.setEndDate(d.getEndDate());
        e.setNextOccurrence(d.getNextOccurrence());
        e.setActive(d.isActive());
        return e;
    }

    @Override
    public Flowable<List<RecurringTransaction>> getAllActive() {
        return dao.getAllActive()
                .map(entities -> entities.stream().map(this::toDomain).collect(Collectors.toList()));
    }

    @Override
    public Single<List<RecurringTransaction>> getDueTransactions(long today) {
        return dao.getDueTransactions(today)
                .map(entities -> entities.stream().map(this::toDomain).collect(Collectors.toList()));
    }

    @Override
    public Single<Long> insert(RecurringTransaction recurring) {
        return dao.insert(toEntity(recurring));
    }

    @Override
    public Completable update(RecurringTransaction recurring) {
        return dao.update(toEntity(recurring));
    }

    @Override
    public Completable delete(RecurringTransaction recurring) {
        return dao.delete(toEntity(recurring));
    }
}
