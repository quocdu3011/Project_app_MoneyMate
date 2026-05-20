package com.example.moneymate.presentation.screen.report;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moneymate.domain.model.Category;
import com.example.moneymate.domain.model.Transaction;
import com.example.moneymate.domain.model.enums.TransactionType;
import com.example.moneymate.domain.repository.CategoryRepository;
import com.example.moneymate.domain.repository.TransactionRepository;
import com.example.moneymate.util.DateUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class ReportViewModel extends ViewModel {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<List<Transaction>> transactions = new MutableLiveData<>();
    private final MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    private final MutableLiveData<Double> totalIncome = new MutableLiveData<>(0.0);
    private final MutableLiveData<Double> totalExpense = new MutableLiveData<>(0.0);
    private final MutableLiveData<Map<Long, Double>> expenseByCategory = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    private int currentMonth = DateUtils.getCurrentMonth();
    private int currentYear = DateUtils.getCurrentYear();

    @Inject
    public ReportViewModel(TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        loadReport();
        loadCategories();
    }

    public void loadReport() {
        long start = DateUtils.getStartOfMonth(currentMonth, currentYear);
        long end = DateUtils.getEndOfMonth(currentMonth, currentYear);

        disposables.add(
            transactionRepository.getTransactionsByDateRange(start, end)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    transactions.setValue(list);
                    double income = list.stream()
                        .filter(t -> t.getType() == TransactionType.INCOME)
                        .mapToDouble(Transaction::getAmount).sum();
                    double expense = list.stream()
                        .filter(t -> t.getType() == TransactionType.EXPENSE)
                        .mapToDouble(Transaction::getAmount).sum();
                    totalIncome.setValue(income);
                    totalExpense.setValue(expense);

                    Map<Long, Double> byCategory = new HashMap<>();
                    for (Transaction t : list) {
                        if (t.getType() == TransactionType.EXPENSE) {
                            byCategory.merge(t.getCategoryId(), t.getAmount(), Double::sum);
                        }
                    }
                    expenseByCategory.setValue(byCategory);
                }, t -> error.setValue(t.getMessage()))
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

    public void setMonthYear(int month, int year) {
        currentMonth = month;
        currentYear = year;
        loadReport();
    }

    public int getCurrentMonth() { return currentMonth; }
    public int getCurrentYear() { return currentYear; }
    public LiveData<List<Transaction>> getTransactions() { return transactions; }
    public LiveData<List<Category>> getCategories() { return categories; }
    public LiveData<Double> getTotalIncome() { return totalIncome; }
    public LiveData<Double> getTotalExpense() { return totalExpense; }
    public LiveData<Map<Long, Double>> getExpenseByCategory() { return expenseByCategory; }
    public LiveData<String> getError() { return error; }

    @Override
    protected void onCleared() {
        disposables.clear();
        super.onCleared();
    }
}
