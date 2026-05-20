package com.example.moneymate.domain.model;

import com.example.moneymate.domain.model.enums.AccountType;

public class Account {
    private long id;
    private String name;
    private AccountType type;
    private double balance;
    private String icon;
    private String color;
    private boolean isDefault;
    private long createdAt;

    public Account() {}

    public Account(long id, String name, AccountType type, double balance,
                   String icon, String color, boolean isDefault, long createdAt) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.balance = balance;
        this.icon = icon;
        this.color = color;
        this.isDefault = isDefault;
        this.createdAt = createdAt;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public AccountType getType() { return type; }
    public void setType(AccountType type) { this.type = type; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean aDefault) { isDefault = aDefault; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
