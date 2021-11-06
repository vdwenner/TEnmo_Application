package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private int transferId;
    private int transferTypeId;
    private int transferStatusId;
    private int accountFrom;
    private int accountTo;
    private BigDecimal amount;

    private int senderId;
    private int receiverId;
    private String senderName;
    private String recName;
    private String transferStatusDescription;
    private String transferTypeDescription;

    public Transfer() {}

    public Transfer(int accountFrom, int accountTo, BigDecimal amount) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getRecName() {
        return recName;
    }

    public void setRecName(String recName) {
        recName = recName;
    }

    public String getTransferStatusDescription() {
        return transferStatusDescription;
    }

    public void setTransferStatusDescription(String transferStatusDescription) {
        this.transferStatusDescription = transferStatusDescription;
    }

    public String getTransferTypeDescription() {
        return transferTypeDescription;
    }

    public void setTransferTypeDescription(String transferTypeDescription) {
        this.transferTypeDescription = transferTypeDescription;
    }

    @Override
    public String toString() {
        return "\n--------------------------------------------" +
                "\n Transfer Details" +
                "\n--------------------------------------------" +
                "\n Id: " + transferId +
                "\n From:'" + senderName +
                "\n To: " + recName +
                "\n Type: " + transferTypeDescription +
                "\n Status: " + transferStatusDescription +
                "\n Amount:" + amount;
    }

}
