package com.example.moneymate.data.local.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.moneymate.data.local.database.dao.AccountDao;
import com.example.moneymate.data.local.database.dao.BudgetDao;
import com.example.moneymate.data.local.database.dao.CategoryDao;
import com.example.moneymate.data.local.database.dao.RecurringTransactionDao;
import com.example.moneymate.data.local.database.dao.TransactionDao;
import com.example.moneymate.data.local.database.entity.AccountEntity;
import com.example.moneymate.data.local.database.entity.BudgetEntity;
import com.example.moneymate.data.local.database.entity.CategoryEntity;
import com.example.moneymate.data.local.database.entity.RecurringTransactionEntity;
import com.example.moneymate.data.local.database.entity.TransactionEntity;

@Database(
    entities = {
        AccountEntity.class,
        CategoryEntity.class,
        TransactionEntity.class,
        BudgetEntity.class,
        RecurringTransactionEntity.class
    },
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "moneymate.db";

    public abstract AccountDao accountDao();
    public abstract CategoryDao categoryDao();
    public abstract TransactionDao transactionDao();
    public abstract BudgetDao budgetDao();
    public abstract RecurringTransactionDao recurringTransactionDao();

    public static AppDatabase create(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }
}
