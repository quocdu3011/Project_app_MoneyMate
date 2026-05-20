package com.example.moneymate.domain.model;

import com.example.moneymate.domain.model.enums.Frequency;

public class RecurringTransaction {
    private long id;
    private long transactionId;
    private Frequency frequency;
    private long startDate;
    private Long endDate;
    private long nextOccurrence;
    private boolean isActive;

    public RecurringTransaction() {}

    public RecurringTransaction(long id, long transactionId, Frequency frequency,
                                long startDate, Long endDate, long nextOccurrence, boolean isActive) {
        this.id = id;
        this.transactionId = transactionId;
        this.frequency = frequency;
        this.startDate = startDate;
        this.endDate = endDate;
        this.nextOccurrence = nextOccurrence;
        this.isActive = isActive;
    }

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
