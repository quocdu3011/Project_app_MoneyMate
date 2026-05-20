package com.example.moneymate.di;

import android.content.Context;

import com.example.moneymate.data.local.database.AppDatabase;
import com.example.moneymate.data.local.database.dao.AccountDao;
import com.example.moneymate.data.local.database.dao.BudgetDao;
import com.example.moneymate.data.local.database.dao.CategoryDao;
import com.example.moneymate.data.local.database.dao.RecurringTransactionDao;
import com.example.moneymate.data.local.database.dao.TransactionDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public AppDatabase provideDatabase(@ApplicationContext Context context) {
        return AppDatabase.create(context);
    }

    @Provides
    @Singleton
    public AccountDao provideAccountDao(AppDatabase db) {
        return db.accountDao();
    }

    @Provides
    @Singleton
    public CategoryDao provideCategoryDao(AppDatabase db) {
        return db.categoryDao();
    }

    @Provides
    @Singleton
    public TransactionDao provideTransactionDao(AppDatabase db) {
        return db.transactionDao();
    }

    @Provides
    @Singleton
    public BudgetDao provideBudgetDao(AppDatabase db) {
        return db.budgetDao();
    }

    @Provides
    @Singleton
    public RecurringTransactionDao provideRecurringTransactionDao(AppDatabase db) {
        return db.recurringTransactionDao();
    }
}
