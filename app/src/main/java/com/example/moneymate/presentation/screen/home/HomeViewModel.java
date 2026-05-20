package com.example.moneymate.presentation.screen.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moneymate.domain.model.Category;
import com.example.moneymate.domain.model.Transaction;
import com.example.moneymate.domain.model.enums.TransactionType;
import com.example.moneymate.domain.repository.AccountRepository;
import com.example.moneymate.domain.repository.CategoryRepository;
import com.example.moneymate.domain.repository.TransactionRepository;
import com.example.moneymate.util.DateUtils;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class HomeViewModel extends ViewModel {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<List<Transaction>> recentTransactions = new MutableLiveData<>();
    private final MutableLiveData<Double> totalBalance = new MutableLiveData<>(0.0);
    private final MutableLiveData<Double> monthlyIncome = new MutableLiveData<>(0.0);
    private final MutableLiveData<Double> monthlyExpense = new MutableLiveData<>(0.0);
    private final MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    @Inject
    public HomeViewModel(TransactionRepository transactionRepository,
                         AccountRepository accountRepository,
                         CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        loadData();
    }

    private void loadData() {
        loadRecentTransactions();
        loadMonthlyTotals();
        loadTotalBalance();
        loadCategories();
    }

    private void loadRecentTransactions() {
        disposables.add(
            transactionRepository.getAllTransactions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    transactions -> recentTransactions.setValue(
                        transactions.size() > 20 ? transactions.subList(0, 20) : transactions),
                    throwable -> error.setValue(throwable.getMessage())
                )
        );
    }

    // Dùng Flowable (getTransactionsByDateRange) để tự động cập nhật khi có giao dịch mới
    private void loadMonthlyTotals() {
        int month = DateUtils.getCurrentMonth();
        int year = DateUtils.getCurrentYear();
        long start = DateUtils.getStartOfMonth(month, year);
        long end = DateUtils.getEndOfMonth(month, year);

        disposables.add(
            transactionRepository.getTransactionsByDateRange(start, end)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(transactions -> {
                    double income = transactions.stream()
                        .filter(t -> t.getType() == TransactionType.INCOME)
                        .mapToDouble(Transaction::getAmount).sum();
                    double expense = transactions.stream()
                        .filter(t -> t.getType() == TransactionType.EXPENSE)
                        .mapToDouble(Transaction::getAmount).sum();
                    monthlyIncome.setValue(income);
                    monthlyExpense.setValue(expense);
                }, throwable -> error.setValue(throwable.getMessage()))
        );
    }

    private void loadTotalBalance() {
        disposables.add(
            accountRepository.getAllAccounts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    accounts -> {
                        double total = accounts.stream().mapToDouble(a -> a.getBalance()).sum();
                        totalBalance.setValue(total);
                    },
                    throwable -> error.setValue(throwable.getMessage())
                )
        );
    }

    private void loadCategories() {
        disposables.add(
            categoryRepository.getAllCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(categories::setValue, t -> error.setValue(t.getMessage()))
        );
    }

    public void refresh() { loadData(); }

    public LiveData<List<Transaction>> getRecentTransactions() { return recentTransactions; }
    public LiveData<Double> getTotalBalance() { return totalBalance; }
    public LiveData<Double> getMonthlyIncome() { return monthlyIncome; }
    public LiveData<Double> getMonthlyExpense() { return monthlyExpense; }
    public LiveData<List<Category>> getCategories() { return categories; }
    public LiveData<String> getError() { return error; }

    @Override
    protected void onCleared() {
        disposables.clear();
        super.onCleared();
    }
}
