package com.example.moneymate.data.local.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.moneymate.data.local.database.entity.CategoryEntity;
import com.example.moneymate.domain.model.enums.TransactionType;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY name ASC")
    Flowable<List<CategoryEntity>> getAllCategories();

    @Query("SELECT * FROM categories WHERE type = :type ORDER BY name ASC")
    Flowable<List<CategoryEntity>> getCategoriesByType(TransactionType type);

    @Query("SELECT * FROM categories WHERE id = :id")
    Single<CategoryEntity> getCategoryById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insert(CategoryEntity category);

    @Update
    Completable update(CategoryEntity category);

    @Delete
    Completable delete(CategoryEntity category);
}
