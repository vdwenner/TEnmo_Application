package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.UserNotFoundException;
import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    public Account getAccount(int userId) throws UserNotFoundException;

    public BigDecimal getBalance(int userId);

    public void addToBalance(BigDecimal addedAmount, int userId) throws UserNotFoundException;

    public void subtractFromBalance(BigDecimal subtractedAmount, int userId) throws UserNotFoundException;

}
