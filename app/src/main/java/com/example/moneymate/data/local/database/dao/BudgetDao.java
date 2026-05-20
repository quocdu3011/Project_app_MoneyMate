package com.example.moneymate.data.local.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.moneymate.data.local.database.entity.BudgetEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface BudgetDao {
    @Query("SELECT * FROM budgets WHERE month = :month AND year = :year")
    Flowable<List<BudgetEntity>> getBudgetsByMonthYear(int month, int year);

    @Query("SELECT * FROM budgets WHERE categoryId = :categoryId AND month = :month AND year = :year LIMIT 1")
    Single<BudgetEntity> getBudgetByCategoryAndMonthYear(long categoryId, int month, int year);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insert(BudgetEntity budget);

    @Update
    Completable update(BudgetEntity budget);

    @Delete
    Completable delete(BudgetEntity budget);

    @Query("UPDATE budgets SET spent = :spent WHERE categoryId = :categoryId AND month = :month AND year = :year")
    Completable updateSpent(long categoryId, int month, int year, double spent);
}
