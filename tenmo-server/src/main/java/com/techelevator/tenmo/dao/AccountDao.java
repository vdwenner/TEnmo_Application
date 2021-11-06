package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    public Account getAccount(int userId);

    public BigDecimal getBalance(int userId);

    public void addToBalance(BigDecimal addedAmount, int userId);

    public void subtractFromBalance(BigDecimal subtractedAmount, int userId);

}
