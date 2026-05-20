package com.example.moneymate.data.mapper;

import com.example.moneymate.data.local.database.entity.CategoryEntity;
import com.example.moneymate.domain.model.Category;

public class CategoryMapper {
    public static Category toDomain(CategoryEntity entity) {
        if (entity == null) return null;
        return new Category(entity.getId(), entity.getName(), entity.getIcon(),
                entity.getColor(), entity.getType(), entity.getParentId(),
                entity.getBudgetLimit(), entity.getCreatedAt());
    }

    public static CategoryEntity toEntity(Category domain) {
        if (domain == null) return null;
        CategoryEntity entity = new CategoryEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setIcon(domain.getIcon());
        entity.setColor(domain.getColor());
        entity.setType(domain.getType());
        entity.setParentId(domain.getParentId());
        entity.setBudgetLimit(domain.getBudgetLimit());
        entity.setCreatedAt(domain.getCreatedAt());
        return entity;
    }
}
