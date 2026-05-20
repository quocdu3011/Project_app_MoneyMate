package com.example.moneymate.domain.repository;

import com.example.moneymate.domain.model.Category;
import com.example.moneymate.domain.model.enums.TransactionType;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface CategoryRepository {
    Flowable<List<Category>> getAllCategories();
    Flowable<List<Category>> getCategoriesByType(TransactionType type);
    Single<Category> getCategoryById(long id);
    Single<Long> insert(Category category);
    Completable update(Category category);
    Completable delete(Category category);
}
