package com.example.moneymate.data.local.database.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.moneymate.domain.model.enums.TransactionType;

@Entity(
    tableName = "transactions",
    foreignKeys = {
        @ForeignKey(entity = AccountEntity.class, parentColumns = "id", childColumns = "accountId", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = CategoryEntity.class, parentColumns = "id", childColumns = "categoryId", onDelete = ForeignKey.SET_NULL)
    },
    indices = {
        @Index(value = {"date"}),
        @Index(value = {"accountId"}),
        @Index(value = {"categoryId"}),
        @Index(value = {"type", "date"})
    }
)
public class TransactionEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private double amount;
    private TransactionType type;
    private String note;
    private long accountId;
    private Long categoryId;
    private long date;
    private long createdAt;
    private long updatedAt;

    public TransactionEntity() {}

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
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public long getDate() { return date; }
    public void setDate(long date) { this.date = date; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
}
