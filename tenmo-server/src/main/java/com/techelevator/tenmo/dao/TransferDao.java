package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    public void balanceTransfer(int accountFrom, int accountTo, BigDecimal amount);

    public List<Transfer> getTransfers();

    public Transfer getTransferById(int transferId);

}
