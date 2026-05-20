package com.example.moneymate.data.local.database.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "budgets",
    foreignKeys = {
        @ForeignKey(entity = CategoryEntity.class, parentColumns = "id", childColumns = "categoryId", onDelete = ForeignKey.CASCADE)
    },
    indices = {
        @Index(value = {"categoryId", "month", "year"}, unique = true)
    }
)
public class BudgetEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long categoryId;
    private double amount;
    private double spent;
    private int month;
    private int year;
    private long createdAt;

    public BudgetEntity() {}

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
