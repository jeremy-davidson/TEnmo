package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;

import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@PreAuthorize("isAuthenticated()")
@RestController

public class TransferController {

    private AccountDao accountDao;
    private TransferDao transferDao;
    private UserDao userDao;

    public TransferController(AccountDao accountDao, TransferDao transferDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    @PostMapping(value = "/transfer")
    public boolean post(Authentication auth, @Valid @RequestBody Transfer transfer) {

        switch (transfer.getTransferType()) {
            case 1:
                //request

                break;
            case 2: return handleSend(auth, transfer);
        }
        return false;
    }

    private boolean handleSend(Authentication auth, Transfer transfer) {
        long senderId = userDao.findIdByUsername(auth.getName());
        User recipient = userDao.findById(transfer.getAccountTo());
        if (senderId != transfer.getAccountFrom() ||
            senderId == transfer.getAccountTo() ||
            recipient == null) {
            return false;
        }

        //throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User registration failed.");
        return true;
    }
}
