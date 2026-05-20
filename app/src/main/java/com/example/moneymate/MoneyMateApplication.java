package com.example.moneymate;

import android.app.Application;

import androidx.hilt.work.HiltWorkerFactory;
import androidx.work.Configuration;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.moneymate.data.local.database.AppDatabase;
import com.example.moneymate.data.local.database.DatabaseSeeder;
import com.example.moneymate.worker.RecurringTransactionWorker;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class MoneyMateApplication extends Application implements Configuration.Provider {

    @Inject
    HiltWorkerFactory workerFactory;

    @Inject
    AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        seedDatabase();
        scheduleRecurringWork();
    }

    private void seedDatabase() {
        DatabaseSeeder.seedIfEmpty(database.categoryDao(), database.accountDao());
    }

    private void scheduleRecurringWork() {
        PeriodicWorkRequest recurringWork = new PeriodicWorkRequest.Builder(
                RecurringTransactionWorker.class, 1, TimeUnit.DAYS)
                .build();
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "recurring_transaction_work",
                ExistingPeriodicWorkPolicy.KEEP,
                recurringWork);
    }

    @Override
    public Configuration getWorkManagerConfiguration() {
        return new Configuration.Builder()
                .setWorkerFactory(workerFactory)
                .build();
    }
}
