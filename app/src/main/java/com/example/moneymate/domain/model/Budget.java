package com.example.moneymate.domain.model;

public class Budget {
    private long id;
    private long categoryId;
    private double amount;
    private double spent;
    private int month;
    private int year;
    private long createdAt;

    public Budget() {}

    public Budget(long id, long categoryId, double amount, double spent,
                  int month, int year, long createdAt) {
        this.id = id;
        this.categoryId = categoryId;
        this.amount = amount;
        this.spent = spent;
        this.month = month;
        this.year = year;
        this.createdAt = createdAt;
    }

    public double getPercentUsed() {
        if (amount <= 0) return 0;
        return (spent / amount) * 100;
    }

    public boolean isOverBudget() {
        return spent >= amount;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getCategoryId() { return categoryId; }
    public void setCategoryId(long categoryId) { this.categoryId = categoryId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public double getSpent() { return spent; }
    public void setSpent(double spent) { this.spent = spent; }
    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
