package com.example.moneymate.domain.model;

import com.example.moneymate.domain.model.enums.TransactionType;

public class Transaction {
    private long id;
    private double amount;
    private TransactionType type;
    private String note;
    private long accountId;
    private long categoryId;
    private long date;
    private long createdAt;
    private long updatedAt;

    public Transaction() {}

    public Transaction(long id, double amount, TransactionType type, String note,
                       long accountId, long categoryId, long date, long createdAt, long updatedAt) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.note = note;
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.date = date;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public long getAccountId() { return accountId; }
    public void setAccountId(long accountId) { this.accountId = accountId; }
    public long getCategoryId() { return categoryId; }
    public void setCategoryId(long categoryId) { this.categoryId = categoryId; }
    public long getDate() { return date; }
    public void setDate(long date) { this.date = date; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
}
