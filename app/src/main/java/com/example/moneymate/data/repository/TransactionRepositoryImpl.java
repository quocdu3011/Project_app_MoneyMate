package com.example.moneymate.data.repository;

import com.example.moneymate.data.local.database.dao.TransactionDao;
import com.example.moneymate.data.mapper.TransactionMapper;
import com.example.moneymate.domain.model.Transaction;
import com.example.moneymate.domain.model.enums.TransactionType;
import com.example.moneymate.domain.repository.TransactionRepository;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class TransactionRepositoryImpl implements TransactionRepository {
    private final TransactionDao transactionDao;

    @Inject
    public TransactionRepositoryImpl(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    @Override
    public Flowable<List<Transaction>> getAllTransactions() {
        return transactionDao.getAllTransactions()
                .map(entities -> entities.stream().map(TransactionMapper::toDomain).collect(Collectors.toList()));
    }

    @Override
    public Flowable<List<Transaction>> getTransactionsByDateRange(long startDate, long endDate) {
        return transactionDao.getTransactionsByDateRange(startDate, endDate)
                .map(entities -> entities.stream().map(TransactionMapper::toDomain).collect(Collectors.toList()));
    }

    @Override
    public Flowable<List<Transaction>> getTransactionsByAccount(long accountId) {
        return transactionDao.getTransactionsByAccount(accountId)
                .map(entities -> entities.stream().map(TransactionMapper::toDomain).collect(Collectors.toList()));
    }

    @Override
    public Flowable<List<Transaction>> getTransactionsByCategory(long categoryId) {
        return transactionDao.getTransactionsByCategory(categoryId)
                .map(entities -> entities.stream().map(TransactionMapper::toDomain).collect(Collectors.toList()));
    }

    @Override
    public Flowable<List<Transaction>> getTransactionsByType(TransactionType type) {
        return transactionDao.getTransactionsByType(type)
                .map(entities -> entities.stream().map(TransactionMapper::toDomain).collect(Collectors.toList()));
    }

    @Override
    public Single<Transaction> getTransactionById(long id) {
        return transactionDao.getTransactionById(id).map(TransactionMapper::toDomain);
    }

    @Override
    public Single<List<Transaction>> searchTransactions(String query) {
        return transactionDao.searchTransactions(query)
                .map(entities -> entities.stream().map(TransactionMapper::toDomain).collect(Collectors.toList()));
    }

    @Override
    public Single<Long> insert(Transaction transaction) {
        return transactionDao.insert(TransactionMapper.toEntity(transaction));
    }

    @Override
    public Completable update(Transaction transaction) {
        return transactionDao.update(TransactionMapper.toEntity(transaction));
    }

    @Override
    public Completable delete(Transaction transaction) {
        return transactionDao.delete(TransactionMapper.toEntity(transaction));
    }

    @Override
    public Single<Double> getTotalByTypeAndDateRange(TransactionType type, long startDate, long endDate) {
        return transactionDao.getTotalByTypeAndDateRange(type, startDate, endDate);
    }
}
