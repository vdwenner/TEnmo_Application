package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    public Account getAccount(int accountId);

    public List<Account> getAllAccounts();

    public Account getAccountByUserId(int userId);

    public BigDecimal getBalanceByAccountId(int accountId);

}
