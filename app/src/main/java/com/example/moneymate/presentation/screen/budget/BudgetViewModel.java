package com.example.moneymate.presentation.screen.budget;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moneymate.domain.model.Budget;
import com.example.moneymate.domain.model.Category;
import com.example.moneymate.domain.repository.BudgetRepository;
import com.example.moneymate.domain.repository.CategoryRepository;
import com.example.moneymate.util.DateUtils;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class BudgetViewModel extends ViewModel {
    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<List<Budget>> budgets = new MutableLiveData<>();
    private final MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    private final MutableLiveData<Boolean> saveSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    private int currentMonth = DateUtils.getCurrentMonth();
    private int currentYear = DateUtils.getCurrentYear();

    @Inject
    public BudgetViewModel(BudgetRepository budgetRepository, CategoryRepository categoryRepository) {
        this.budgetRepository = budgetRepository;
        this.categoryRepository = categoryRepository;
        loadBudgets();
        loadCategories();
    }

    public void loadBudgets() {
        disposables.add(
            budgetRepository.getBudgetsByMonthYear(currentMonth, currentYear)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(budgets::setValue, t -> error.setValue(t.getMessage()))
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

    public void saveBudget(Budget budget) {
        budget.setCreatedAt(DateUtils.now());
        disposables.add(
            budgetRepository.insert(budget)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(id -> saveSuccess.setValue(true), t -> error.setValue(t.getMessage()))
        );
    }

    public void deleteBudget(Budget budget) {
        disposables.add(
            budgetRepository.delete(budget)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, t -> error.setValue(t.getMessage()))
        );
    }

    public void setMonthYear(int month, int year) {
        currentMonth = month;
        currentYear = year;
        loadBudgets();
    }

    public int getCurrentMonth() { return currentMonth; }
    public int getCurrentYear() { return currentYear; }
    public LiveData<List<Budget>> getBudgets() { return budgets; }
    public LiveData<List<Category>> getCategories() { return categories; }
    public LiveData<Boolean> getSaveSuccess() { return saveSuccess; }
    public LiveData<String> getError() { return error; }

    @Override
    protected void onCleared() {
        disposables.clear();
        super.onCleared();
    }
}
