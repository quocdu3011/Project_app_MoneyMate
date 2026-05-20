package com.example.moneymate.data.mapper;

import com.example.moneymate.data.local.database.entity.AccountEntity;
import com.example.moneymate.domain.model.Account;

public class AccountMapper {
    public static Account toDomain(AccountEntity entity) {
        if (entity == null) return null;
        return new Account(entity.getId(), entity.getName(), entity.getType(),
                entity.getBalance(), entity.getIcon(), entity.getColor(),
                entity.isDefault(), entity.getCreatedAt());
    }

    public static AccountEntity toEntity(Account domain) {
        if (domain == null) return null;
        AccountEntity entity = new AccountEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setType(domain.getType());
        entity.setBalance(domain.getBalance());
        entity.setIcon(domain.getIcon());
        entity.setColor(domain.getColor());
        entity.setDefault(domain.isDefault());
        entity.setCreatedAt(domain.getCreatedAt());
        return entity;
    }
}
