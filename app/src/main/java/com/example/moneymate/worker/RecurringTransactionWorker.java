package com.example.moneymate.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.hilt.work.HiltWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.moneymate.domain.model.Transaction;
import com.example.moneymate.domain.model.RecurringTransaction;
import com.example.moneymate.domain.model.enums.Frequency;
import com.example.moneymate.domain.repository.AccountRepository;
import com.example.moneymate.domain.repository.RecurringTransactionRepository;
import com.example.moneymate.domain.repository.TransactionRepository;
import com.example.moneymate.domain.model.enums.TransactionType;
import com.example.moneymate.util.DateUtils;

import java.util.Calendar;
import java.util.List;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltWorker
public class RecurringTransactionWorker extends Worker {
    private final RecurringTransactionRepository recurringRepo;
    private final TransactionRepository transactionRepo;
    private final AccountRepository accountRepo;

    @AssistedInject
    public RecurringTransactionWorker(
            @Assisted @NonNull Context context,
            @Assisted @NonNull WorkerParameters params,
            RecurringTransactionRepository recurringRepo,
            TransactionRepository transactionRepo,
            AccountRepository accountRepo) {
        super(context, params);
        this.recurringRepo = recurringRepo;
        this.transactionRepo = transactionRepo;
        this.accountRepo = accountRepo;
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            long today = DateUtils.getStartOfDay(System.currentTimeMillis());
            List<RecurringTransaction> dueList = recurringRepo
                    .getDueTransactions(today)
                    .subscribeOn(Schedulers.io())
                    .blockingGet();

            for (RecurringTransaction recurring : dueList) {
                Transaction template = transactionRepo
                        .getTransactionById(recurring.getTransactionId())
                        .subscribeOn(Schedulers.io())
                        .blockingGet();

                Transaction newTx = new Transaction();
                newTx.setAmount(template.getAmount());
                newTx.setType(template.getType());
                newTx.setNote(template.getNote());
                newTx.setAccountId(template.getAccountId());
                newTx.setCategoryId(template.getCategoryId());
                newTx.setDate(System.currentTimeMillis());
                newTx.setCreatedAt(System.currentTimeMillis());
                newTx.setUpdatedAt(System.currentTimeMillis());

                transactionRepo.insert(newTx).subscribeOn(Schedulers.io()).blockingGet();

                accountRepo.getAccountById(template.getAccountId())
                        .subscribeOn(Schedulers.io())
                        .flatMapCompletable(account -> {
                            double newBalance = template.getType() == TransactionType.INCOME
                                    ? account.getBalance() + template.getAmount()
                                    : account.getBalance() - template.getAmount();
                            return accountRepo.updateBalance(account.getId(), newBalance);
                        }).subscribeOn(Schedulers.io()).blockingAwait();

                recurring.setNextOccurrence(calculateNext(recurring));
                recurringRepo.update(recurring).subscribeOn(Schedulers.io()).blockingAwait();
            }
            return Result.success();
        } catch (Exception e) {
            return Result.failure();
        }
    }

    private long calculateNext(RecurringTransaction recurring) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(recurring.getNextOccurrence());
        switch (recurring.getFrequency()) {
            case DAILY:   cal.add(Calendar.DAY_OF_YEAR, 1); break;
            case WEEKLY:  cal.add(Calendar.WEEK_OF_YEAR, 1); break;
            case MONTHLY: cal.add(Calendar.MONTH, 1); break;
            case YEARLY:  cal.add(Calendar.YEAR, 1); break;
        }
        return cal.getTimeInMillis();
    }
}
