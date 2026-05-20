package com.example.moneymate.data.mapper;

import com.example.moneymate.data.local.database.entity.BudgetEntity;
import com.example.moneymate.domain.model.Budget;

public class BudgetMapper {
    public static Budget toDomain(BudgetEntity entity) {
        if (entity == null) return null;
        return new Budget(entity.getId(), entity.getCategoryId(), entity.getAmount(),
                entity.getSpent(), entity.getMonth(), entity.getYear(), entity.getCreatedAt());
    }

    public static BudgetEntity toEntity(Budget domain) {
        if (domain == null) return null;
        BudgetEntity entity = new BudgetEntity();
        entity.setId(domain.getId());
        entity.setCategoryId(domain.getCategoryId());
        entity.setAmount(domain.getAmount());
        entity.setSpent(domain.getSpent());
        entity.setMonth(domain.getMonth());
        entity.setYear(domain.getYear());
        entity.setCreatedAt(domain.getCreatedAt());
        return entity;
    }
}
