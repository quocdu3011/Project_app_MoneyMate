package com.example.moneymate.data.local.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.moneymate.data.local.database.entity.AccountEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface AccountDao {
    @Query("SELECT * FROM accounts ORDER BY isDefault DESC, name ASC")
    Flowable<List<AccountEntity>> getAllAccounts();

    @Query("SELECT * FROM accounts WHERE id = :id")
    Single<AccountEntity> getAccountById(long id);

    @Query("SELECT * FROM accounts WHERE isDefault = 1 LIMIT 1")
    Single<AccountEntity> getDefaultAccount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insert(AccountEntity account);

    @Update
    Completable update(AccountEntity account);

    @Delete
    Completable delete(AccountEntity account);

    @Query("UPDATE accounts SET balance = :newBalance WHERE id = :accountId")
    Completable updateBalance(long accountId, double newBalance);
}
