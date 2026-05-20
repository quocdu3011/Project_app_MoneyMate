package com.example.moneymate.data.local.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.moneymate.data.local.database.entity.RecurringTransactionEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface RecurringTransactionDao {
    @Query("SELECT * FROM recurring_transactions WHERE isActive = 1")
    Flowable<List<RecurringTransactionEntity>> getAllActive();

    @Query("SELECT * FROM recurring_transactions WHERE isActive = 1 AND nextOccurrence <= :today")
    Single<List<RecurringTransactionEntity>> getDueTransactions(long today);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insert(RecurringTransactionEntity recurring);

    @Update
    Completable update(RecurringTransactionEntity recurring);

    @Delete
    Completable delete(RecurringTransactionEntity recurring);
}
