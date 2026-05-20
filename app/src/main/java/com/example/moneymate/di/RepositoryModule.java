package com.example.moneymate.di;

import com.example.moneymate.data.repository.AccountRepositoryImpl;
import com.example.moneymate.data.repository.BudgetRepositoryImpl;
import com.example.moneymate.data.repository.CategoryRepositoryImpl;
import com.example.moneymate.data.repository.RecurringTransactionRepositoryImpl;
import com.example.moneymate.data.repository.TransactionRepositoryImpl;
import com.example.moneymate.domain.repository.AccountRepository;
import com.example.moneymate.domain.repository.BudgetRepository;
import com.example.moneymate.domain.repository.CategoryRepository;
import com.example.moneymate.domain.repository.RecurringTransactionRepository;
import com.example.moneymate.domain.repository.TransactionRepository;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public abstract class RepositoryModule {

    @Binds
    @Singleton
    public abstract AccountRepository bindAccountRepository(AccountRepositoryImpl impl);

    @Binds
    @Singleton
    public abstract CategoryRepository bindCategoryRepository(CategoryRepositoryImpl impl);

    @Binds
    @Singleton
    public abstract TransactionRepository bindTransactionRepository(TransactionRepositoryImpl impl);

    @Binds
    @Singleton
    public abstract BudgetRepository bindBudgetRepository(BudgetRepositoryImpl impl);

    @Binds
    @Singleton
    public abstract RecurringTransactionRepository bindRecurringTransactionRepository(RecurringTransactionRepositoryImpl impl);
}
