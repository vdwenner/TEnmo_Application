package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.apache.coyote.Request;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/accounts/")
public class AccountController {
    private AccountDao dao;

    public AccountController() {
        this.dao = new JdbcAccountDao;

    }

    @RequestMapping(path = "balance", method = RequestMethod.GET)
    public Account getAccount(@AuthenticationPrincipal principal) {
        return dao.getAccount();
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    public Account updateAccount(@PathVariable )

}
