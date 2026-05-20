package com.example.moneymate.data.mapper;

import com.example.moneymate.data.local.database.entity.TransactionEntity;
import com.example.moneymate.domain.model.Transaction;

public class TransactionMapper {
    public static Transaction toDomain(TransactionEntity entity) {
        if (entity == null) return null;
        Transaction t = new Transaction();
        t.setId(entity.getId());
        t.setAmount(entity.getAmount());
        t.setType(entity.getType());
        t.setNote(entity.getNote());
        t.setAccountId(entity.getAccountId());
        t.setCategoryId(entity.getCategoryId() != null ? entity.getCategoryId() : 0);
        t.setDate(entity.getDate());
        t.setCreatedAt(entity.getCreatedAt());
        t.setUpdatedAt(entity.getUpdatedAt());
        return t;
    }

    public static TransactionEntity toEntity(Transaction domain) {
        if (domain == null) return null;
        TransactionEntity entity = new TransactionEntity();
        entity.setId(domain.getId());
        entity.setAmount(domain.getAmount());
        entity.setType(domain.getType());
        entity.setNote(domain.getNote());
        entity.setAccountId(domain.getAccountId());
        entity.setCategoryId(domain.getCategoryId() > 0 ? domain.getCategoryId() : null);
        entity.setDate(domain.getDate());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }
}
