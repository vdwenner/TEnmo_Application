package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.exception.TransferNotFoundException;
import com.techelevator.tenmo.exception.UserNotFoundException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping(path = "account")
public class TenmoController {


    private TransferDao transferDao;
    private AccountDao accountDao;
    private UserDao userDao;


    public TenmoController(JdbcAccountDao accountDao, JdbcUserDao userDao, JdbcTransferDao transferDao) {

        this.accountDao = accountDao;
        this.userDao = userDao;
        this.transferDao = transferDao;

    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> listAllUsers(Principal currentUser) {
        return userDao.findAll();
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(path = "balance", method = RequestMethod.GET)
    public BigDecimal getAccountBalance(Principal currentUser) throws UserNotFoundException {
        return accountDao.getAccount(userDao.findIdByUsername(currentUser.getName())).getBalance();
    }

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public void createTransfer(@RequestBody Transfer transfer) throws UserNotFoundException {
        transferDao.sendTransfer(transfer.getSenderId(),transfer.getReceiverId(),transfer.getAmount());
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(path = "/allTransfers", method = RequestMethod.GET)
    public List<Transfer> getAllUserTransfers(Principal currentUser){
        List<Transfer> allTransfers = transferDao.getAllUserTransfers(userDao.findIdByUsername(currentUser.getName()));
        return allTransfers;
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(path = "/transfer/{transferId}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable  int transferId) throws TransferNotFoundException {
        return transferDao.getTransferById(transferId);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(path = "/updateTransfer", method = RequestMethod.PUT)
    public void update(@RequestBody Transfer transfer) throws TransferNotFoundException, UserNotFoundException {
        transferDao.updateTransferRequest(transfer, transfer.getTransferStatusDescription());
    }

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfer/request", method = RequestMethod.POST)
    public String requestTransfer(@Valid @RequestBody Transfer transfer) throws UserNotFoundException {
        return transferDao.requestTransfer(transfer.getSenderId(),transfer.getReceiverId(),transfer.getAmount());
    }

}
