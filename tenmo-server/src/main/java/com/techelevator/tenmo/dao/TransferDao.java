package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    public void runTransaction(Transfer transfer);

    public List<Transfer> getAllTransfers();

    public Transfer getTransferById(int transferId);

    public List<Transfer> getAllTransferByUSerId(int userId);

    public void createTransfer(int accountFrom, int accountTo, BigDecimal amount);

    public void updateTransferTypeToRequest(int transferId);

    public void updateTransferTypeToSend(int transferId);

    public void updateTransferStatusToPending(int transferId);

    public void updateTransferStatusToApproved(int transferId);

    public void updateTransferStatusToRejected(int transferId);

}
