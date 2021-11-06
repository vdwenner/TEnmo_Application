package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> listAllUsers(Principal currentUser) {
        return userDao.findAll();
    }

    @RequestMapping(path = "balance", method = RequestMethod.GET)
    public BigDecimal getAccountBalance(Principal currentUser) {
        return accountDao.getAccount(userDao.findIdByUsername(currentUser.getName())).getBalance();
    }

    @PreAuthorize("permitAll")
    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public void createTransfer(@RequestBody Transfer transfer){
        transferDao.sendTransfer(transfer.getSenderId(),transfer.getReceiverId(),transfer.getAmount());
    }

    @RequestMapping(path = "/allTransfers", method = RequestMethod.GET)
    public List<Transfer> getAllUserTransfers(Principal currentUser){
        List<Transfer> allTransfers = transferDao.getAllUserTransfers(userDao.findIdByUsername(currentUser.getName()));
        return allTransfers;
    }

    @RequestMapping(path = "/transfer/{transferId}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable  int transferId) {
        return transferDao.getTransferById(transferId);
    }

}
