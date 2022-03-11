package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import com.techelevator.tenmo.model.TransferHistoryItem;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

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

    @GetMapping(value = "/transfer")
    public TransferHistoryItem[] get(Authentication auth){
        //TODO: try to combine this line and next line into one SQL query
            //e.g. "getAccountIdByUsername"
        long userId = userDao.findIdByUsername(auth.getName());
        long accountId = accountDao.findByUserId(userId).getAccountId();
        List<TransferHistoryItem> transfers = transferDao.getHistoryByAccountId(accountId);
        return transfers.toArray(new TransferHistoryItem[0]);
    }

    @GetMapping(value = "/transfer/{id}")
    public Transfer getTransferById(){
        return null;
    }

    private boolean handleSend(Authentication auth, Transfer transfer) {
        long senderId = userDao.findIdByUsername(auth.getName());
        User recipient = userDao.findById(transfer.getAccountTo());

        //Validate Send Transfer is valid.
        if (senderId != transfer.getAccountFrom()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You must send money from your own account.");
        }
        if (senderId == transfer.getAccountTo() ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You can not send money to yourself.");
        }
        if (recipient == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Recipient not found in system.");
        }

        Account senderAccount = accountDao.findByUserId(senderId);
        int result = senderAccount.getBalance().compareTo(transfer.getAmount());
        if (result == -1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Insufficient funds to send.");
        }

        //convert from userIDs to accountIDs
        transfer.setAccountFrom(accountDao.findByUserId(senderId).getAccountId());
        transfer.setAccountTo(accountDao.findByUserId(recipient.getId()).getAccountId());

        //create transfer record
        Long result1 = transferDao.create(transfer);
        if (result1 == -1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Transfer could not be created in system.");
        }
        transfer.setTransferId(result1);

        //perform transfer
        return accountDao.performTransferOnAccounts(transfer);
    }
}
