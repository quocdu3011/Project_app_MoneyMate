package com.example.moneymate.domain.repository;

import com.example.moneymate.domain.model.Account;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface AccountRepository {
    Flowable<List<Account>> getAllAccounts();
    Single<Account> getAccountById(long id);
    Single<Account> getDefaultAccount();
    Single<Long> insert(Account account);
    Completable update(Account account);
    Completable delete(Account account);
    Completable updateBalance(long accountId, double newBalance);
}
