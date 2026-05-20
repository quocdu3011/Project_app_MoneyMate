package com.example.moneymate.presentation.screen.category;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moneymate.domain.model.Category;
import com.example.moneymate.domain.model.enums.TransactionType;
import com.example.moneymate.domain.repository.CategoryRepository;
import com.example.moneymate.util.DateUtils;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class CategoryViewModel extends ViewModel {
    private final CategoryRepository categoryRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    private final MutableLiveData<Boolean> saveSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    @Inject
    public CategoryViewModel(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        loadCategories();
    }

    private void loadCategories() {
        disposables.add(
            categoryRepository.getAllCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(categories::setValue, t -> error.setValue(t.getMessage()))
        );
    }

    public void saveCategory(Category category) {
        category.setCreatedAt(DateUtils.now());
        disposables.add(
            categoryRepository.insert(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(id -> saveSuccess.setValue(true), t -> error.setValue(t.getMessage()))
        );
    }

    public void deleteCategory(Category category) {
        disposables.add(
            categoryRepository.delete(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, t -> error.setValue(t.getMessage()))
        );
    }

    public LiveData<List<Category>> getCategories() { return categories; }
    public LiveData<Boolean> getSaveSuccess() { return saveSuccess; }
    public LiveData<String> getError() { return error; }

    @Override
    protected void onCleared() {
        disposables.clear();
        super.onCleared();
    }
}
