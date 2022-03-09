package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {
    //Instance Properties
    private long accountId;
    private long userId;
    private BigDecimal balance;

    //Constructor
    public Account(){}
    public Account(long accountId, long userId, BigDecimal balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    }

    //Getters and Setters
    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }
}
