package com.example.moneymate.data.repository;

import com.example.moneymate.data.local.database.dao.CategoryDao;
import com.example.moneymate.data.mapper.CategoryMapper;
import com.example.moneymate.domain.model.Category;
import com.example.moneymate.domain.model.enums.TransactionType;
import com.example.moneymate.domain.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class CategoryRepositoryImpl implements CategoryRepository {
    private final CategoryDao categoryDao;

    @Inject
    public CategoryRepositoryImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public Flowable<List<Category>> getAllCategories() {
        return categoryDao.getAllCategories()
                .map(entities -> entities.stream().map(CategoryMapper::toDomain).collect(Collectors.toList()));
    }

    @Override
    public Flowable<List<Category>> getCategoriesByType(TransactionType type) {
        return categoryDao.getCategoriesByType(type)
                .map(entities -> entities.stream().map(CategoryMapper::toDomain).collect(Collectors.toList()));
    }

    @Override
    public Single<Category> getCategoryById(long id) {
        return categoryDao.getCategoryById(id).map(CategoryMapper::toDomain);
    }

    @Override
    public Single<Long> insert(Category category) {
        return categoryDao.insert(CategoryMapper.toEntity(category));
    }

    @Override
    public Completable update(Category category) {
        return categoryDao.update(CategoryMapper.toEntity(category));
    }

    @Override
    public Completable delete(Category category) {
        return categoryDao.delete(CategoryMapper.toEntity(category));
    }
}
