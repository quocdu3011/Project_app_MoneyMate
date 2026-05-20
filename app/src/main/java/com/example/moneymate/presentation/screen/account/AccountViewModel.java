package com.example.moneymate.presentation.screen.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moneymate.domain.model.Account;
import com.example.moneymate.domain.model.enums.AccountType;
import com.example.moneymate.domain.repository.AccountRepository;
import com.example.moneymate.util.DateUtils;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class AccountViewModel extends ViewModel {
    private final AccountRepository accountRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<List<Account>> accounts = new MutableLiveData<>();
    private final MutableLiveData<Double> totalBalance = new MutableLiveData<>(0.0);
    private final MutableLiveData<Boolean> saveSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    @Inject
    public AccountViewModel(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        loadAccounts();
    }

    private void loadAccounts() {
        disposables.add(
            accountRepository.getAllAccounts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    accounts.setValue(list);
                    double total = list.stream().mapToDouble(Account::getBalance).sum();
                    totalBalance.setValue(total);
                }, t -> error.setValue(t.getMessage()))
        );
    }

    public void saveAccount(Account account) {
        account.setCreatedAt(DateUtils.now());
        disposables.add(
            accountRepository.insert(account)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(id -> saveSuccess.setValue(true), t -> error.setValue(t.getMessage()))
        );
    }

    public void deleteAccount(Account account) {
        disposables.add(
            accountRepository.delete(account)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, t -> error.setValue(t.getMessage()))
        );
    }

    public LiveData<List<Account>> getAccounts() { return accounts; }
    public LiveData<Double> getTotalBalance() { return totalBalance; }
    public LiveData<Boolean> getSaveSuccess() { return saveSuccess; }
    public LiveData<String> getError() { return error; }

    @Override
    protected void onCleared() {
        disposables.clear();
        super.onCleared();
    }
}
