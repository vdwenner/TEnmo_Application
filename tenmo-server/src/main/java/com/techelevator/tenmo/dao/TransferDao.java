package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    public String sendTransfer(int senderId, int receiverId, BigDecimal amount);

    public String requestTransfer(int senderId, int receiverId, BigDecimal amount);

    public Transfer getTransferById(int transferId);

    public int getTransferTypeId(String type);

    public int getTransferStatusId(String status);

    public List<Transfer> getAllUserTransfers(int userId);

    public List<Transfer> getPendingRequests(int userId);


}
