package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.apache.coyote.Request;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/accounts/")
public class TenmoController {

    private TransferDao transferDao;
    private AccountDao accountDao;
    private UserDao userDao;

    public TenmoController(TransferDao transferDao, AccountDao accountDao, JdbcUserDao userDao) {

        this.transferDao = transferDao;
        this.accountDao = accountDao;
        this.userDao = userDao;

    }

    @RequestMapping(path = "balance", method = RequestMethod.GET)
    public BigDecimal getAccountBalance(Principal currentUser) {

        return accountDao.getAccountByUserId(userDao.findIdByUsername(currentUser.getName())).getBalance();
    }

//    @PreAuthorize("permitAll")
//    @RequestMapping(path = "transfers", method = RequestMethod.POST)
//    public void createTransfer(@RequestBody Transfer transfer) {
//
//        transferdao.createTransfer(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
//    }

    //USER
    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> listAllUsers(Principal currentUser) {
        return userDao.findAll();
    }


    //TRANSFER
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

//    @RequestMapping(path = "/transfer/request", method = RequestMethod.POST)
//    public String requestTransfer(@RequestBody Transfer transfer){
//        return null;
//    }


//    @RequestMapping(path = "/transfer/pending", method = RequestMethod.POST)
//    public ist<Transfer> getPendingRequests(int userId){
//        return null;
//    }

}
