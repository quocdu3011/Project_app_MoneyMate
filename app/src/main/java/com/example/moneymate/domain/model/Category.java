package com.example.moneymate.domain.model;

import com.example.moneymate.domain.model.enums.TransactionType;

public class Category {
    private long id;
    private String name;
    private String icon;
    private String color;
    private TransactionType type;
    private Long parentId;
    private Double budgetLimit;
    private long createdAt;

    public Category() {}

    public Category(long id, String name, String icon, String color, TransactionType type,
                    Long parentId, Double budgetLimit, long createdAt) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.color = color;
        this.type = type;
        this.parentId = parentId;
        this.budgetLimit = budgetLimit;
        this.createdAt = createdAt;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public Double getBudgetLimit() { return budgetLimit; }
    public void setBudgetLimit(Double budgetLimit) { this.budgetLimit = budgetLimit; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
