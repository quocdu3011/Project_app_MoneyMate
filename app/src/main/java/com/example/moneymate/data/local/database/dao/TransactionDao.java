package com.example.moneymate.data.local.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.moneymate.data.local.database.entity.TransactionEntity;
import com.example.moneymate.domain.model.enums.TransactionType;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    Flowable<List<TransactionEntity>> getAllTransactions();

    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    Flowable<List<TransactionEntity>> getTransactionsByDateRange(long startDate, long endDate);

    @Query("SELECT * FROM transactions WHERE accountId = :accountId ORDER BY date DESC")
    Flowable<List<TransactionEntity>> getTransactionsByAccount(long accountId);

    @Query("SELECT * FROM transactions WHERE categoryId = :categoryId ORDER BY date DESC")
    Flowable<List<TransactionEntity>> getTransactionsByCategory(long categoryId);

    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY date DESC")
    Flowable<List<TransactionEntity>> getTransactionsByType(TransactionType type);

    @Query("SELECT * FROM transactions WHERE id = :id")
    Single<TransactionEntity> getTransactionById(long id);

    @Query("SELECT * FROM transactions WHERE note LIKE '%' || :query || '%' ORDER BY date DESC")
    Single<List<TransactionEntity>> searchTransactions(String query);

    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = :type AND date BETWEEN :startDate AND :endDate")
    Single<Double> getTotalByTypeAndDateRange(TransactionType type, long startDate, long endDate);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insert(TransactionEntity transaction);

    @Update
    Completable update(TransactionEntity transaction);

    @Delete
    Completable delete(TransactionEntity transaction);
}
