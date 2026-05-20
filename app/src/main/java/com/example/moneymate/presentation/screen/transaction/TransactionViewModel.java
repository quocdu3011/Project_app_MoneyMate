package com.example.moneymate.presentation.screen.transaction;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moneymate.domain.model.Account;
import com.example.moneymate.domain.model.Category;
import com.example.moneymate.domain.model.Transaction;
import com.example.moneymate.domain.model.enums.TransactionType;
import com.example.moneymate.domain.repository.AccountRepository;
import com.example.moneymate.domain.repository.BudgetRepository;
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
public class TransactionViewModel extends ViewModel {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final BudgetRepository budgetRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<List<Transaction>> transactions = new MutableLiveData<>();
    private final MutableLiveData<List<Account>> accounts = new MutableLiveData<>();
    private final MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    private final MutableLiveData<Boolean> saveSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Transaction> selectedTransaction = new MutableLiveData<>();

    @Inject
    public TransactionViewModel(TransactionRepository transactionRepository,
                                AccountRepository accountRepository,
                                CategoryRepository categoryRepository,
                                BudgetRepository budgetRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.budgetRepository = budgetRepository;
        loadTransactions();
        loadAccounts();
        loadCategories();
    }

    private void loadTransactions() {
        disposables.add(
            transactionRepository.getAllTransactions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(transactions::setValue, t -> error.setValue(t.getMessage()))
        );
    }

    private void loadAccounts() {
        disposables.add(
            accountRepository.getAllAccounts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(accounts::setValue, t -> error.setValue(t.getMessage()))
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

    public void saveTransaction(Transaction transaction) {
        long now = DateUtils.now();
        transaction.setCreatedAt(now);
        transaction.setUpdatedAt(now);

        disposables.add(
            transactionRepository.insert(transaction)
                .flatMapCompletable(id -> {
                    transaction.setId(id);
                    return accountRepository.getAccountById(transaction.getAccountId())
                        .flatMapCompletable(account -> {
                            double newBalance = transaction.getType() == TransactionType.INCOME
                                ? account.getBalance() + transaction.getAmount()
                                : account.getBalance() - transaction.getAmount();
                            return accountRepository.updateBalance(account.getId(), newBalance);
                        });
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> saveSuccess.setValue(true), t -> error.setValue(t.getMessage()))
        );
    }

    public void deleteTransaction(Transaction transaction) {
        disposables.add(
            transactionRepository.delete(transaction)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, t -> error.setValue(t.getMessage()))
        );
    }

    public LiveData<List<Transaction>> getTransactions() { return transactions; }
    public LiveData<List<Account>> getAccounts() { return accounts; }
    public LiveData<List<Category>> getCategories() { return categories; }
    public LiveData<Boolean> getSaveSuccess() { return saveSuccess; }
    public LiveData<String> getError() { return error; }
    public LiveData<Transaction> getSelectedTransaction() { return selectedTransaction; }

    public void loadTransaction(long id) {
        disposables.add(
            transactionRepository.getTransactionById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(selectedTransaction::setValue, t -> error.setValue(t.getMessage()))
        );
    }

    @Override
    protected void onCleared() {
        disposables.clear();
        super.onCleared();
    }
}
