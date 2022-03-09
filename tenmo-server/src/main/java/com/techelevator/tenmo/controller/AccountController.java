package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;

@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {

    private final AccountDao accountDao = new JdbcAccountDao();


    @GetMapping(value = "/account")
    public Account get(Authentication auth, @RequestHeader("userId") String userId){
        System.out.println(userId);
        BigDecimal bal = new BigDecimal(2.45);
        Account account = new Account(1,1, bal);
        return account;
    }

}
