package com.example.moneymate.data.repository;

import com.example.moneymate.data.local.database.dao.AccountDao;
import com.example.moneymate.data.mapper.AccountMapper;
import com.example.moneymate.domain.model.Account;
import com.example.moneymate.domain.repository.AccountRepository;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class AccountRepositoryImpl implements AccountRepository {
    private final AccountDao accountDao;

    @Inject
    public AccountRepositoryImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public Flowable<List<Account>> getAllAccounts() {
        return accountDao.getAllAccounts()
                .map(entities -> entities.stream().map(AccountMapper::toDomain).collect(Collectors.toList()));
    }

    @Override
    public Single<Account> getAccountById(long id) {
        return accountDao.getAccountById(id).map(AccountMapper::toDomain);
    }

    @Override
    public Single<Account> getDefaultAccount() {
        return accountDao.getDefaultAccount().map(AccountMapper::toDomain);
    }

    @Override
    public Single<Long> insert(Account account) {
        return accountDao.insert(AccountMapper.toEntity(account));
    }

    @Override
    public Completable update(Account account) {
        return accountDao.update(AccountMapper.toEntity(account));
    }

    @Override
    public Completable delete(Account account) {
        return accountDao.delete(AccountMapper.toEntity(account));
    }

    @Override
    public Completable updateBalance(long accountId, double newBalance) {
        return accountDao.updateBalance(accountId, newBalance);
    }
}
