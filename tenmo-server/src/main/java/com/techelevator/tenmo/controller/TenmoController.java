package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
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

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/accounts/")
public class TenmoController {

    private TransferDao transferdao;
    private AccountDao accountDao;
    private UserDao userDao;

    public TenmoController(TransferDao transferDao, AccountDao accountDao, JdbcUserDao userDao) {

        this.transferdao = transferDao;
        this.accountDao = accountDao;
        this.userDao = userDao;

    }

    @RequestMapping(path = "balance", method = RequestMethod.GET)
    public BigDecimal getAccountBalance(Principal currentUser) {

        return accountDao.getAccountByUserId(userDao.findIdByUsername(currentUser.getName())).getBalance();
    }

    @PreAuthorize("permitAll")
    @RequestMapping(path = "transfers", method = RequestMethod.POST)
    public void createTransfer(@RequestBody Transfer transfer) {

        transferdao.createTransfer(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
    }


}
