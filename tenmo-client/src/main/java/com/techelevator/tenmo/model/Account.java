package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Account {

    private int accountId;
    private int userId;
    private BigDecimal balance;
    private String username;

    public int getAccountId() { return this.accountId; }

    public int getUserId() { return this.userId; }

    public BigDecimal getBalance() { return this.balance; }

    public String getUsername() {
        return username;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Account() {}

    public Account(int accountId, int userId, BigDecimal balance, String username) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
        this.username = username;
    }
}
