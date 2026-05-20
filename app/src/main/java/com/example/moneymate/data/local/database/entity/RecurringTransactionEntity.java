package com.example.moneymate.data.local.database.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.moneymate.domain.model.enums.Frequency;

@Entity(
    tableName = "recurring_transactions",
    foreignKeys = {
        @ForeignKey(entity = TransactionEntity.class, parentColumns = "id", childColumns = "transactionId", onDelete = ForeignKey.CASCADE)
    },
    indices = { @Index(value = {"transactionId"}) }
)
public class RecurringTransactionEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long transactionId;
    private Frequency frequency;
    private long startDate;
    private Long endDate;
    private long nextOccurrence;
    private boolean isActive;

    public RecurringTransactionEntity() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getTransactionId() { return transactionId; }
    public void setTransactionId(long transactionId) { this.transactionId = transactionId; }
    public Frequency getFrequency() { return frequency; }
    public void setFrequency(Frequency frequency) { this.frequency = frequency; }
    public long getStartDate() { return startDate; }
    public void setStartDate(long startDate) { this.startDate = startDate; }
    public Long getEndDate() { return endDate; }
    public void setEndDate(Long endDate) { this.endDate = endDate; }
    public long getNextOccurrence() { return nextOccurrence; }
    public void setNextOccurrence(long nextOccurrence) { this.nextOccurrence = nextOccurrence; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
