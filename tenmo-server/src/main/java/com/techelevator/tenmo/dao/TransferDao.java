package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.TransferNotFoundException;
import com.techelevator.tenmo.exception.UserNotFoundException;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    public String sendTransfer(int senderId, int receiverId, BigDecimal amount) throws UserNotFoundException;

    public Transfer getTransferById(int transferId) throws TransferNotFoundException;

    public int getTransferTypeId(String type);

    public int getTransferStatusId(String status);

    public List<Transfer> getAllUserTransfers(int userId);

    public void updateTransferRequest(Transfer transfer, String status) throws UserNotFoundException, TransferNotFoundException;

    public String requestTransfer(int senderId, int receiverId, BigDecimal amount) throws UserNotFoundException;


}
