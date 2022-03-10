package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.util.ArrayList;
import java.util.List;

public class TransferService {

    public static String[] formatTransferStrings(Transfer[] transfers, User[] users, Account[] accounts, User currentUser) {
        List<String> strings = new ArrayList<>();
        String formatString = "%1d%13s%12s%12s%7.2d%n";

        for (Transfer t: transfers) {
            long fromAccount = 0;
            long currentUserAccount = 0;
            for (Account a : accounts){
                if(a.getUserId() == t.getAccountFrom()){
                    fromAccount = a.getAccountId();
                }
                if(a.getUserId() == currentUser.getId()){
                    currentUserAccount = a.getAccountId();
                }
            }

            boolean from = currentUserAccount == fromAccount;

            String otherUser = "";
            long id = from ? t.getAccountTo() : t.getAccountFrom();

            for (Account a: accounts){
                if(a.getAccountId() == id){
                    id = a.getUserId();
                }
            }

            for (User u: users){
                if(u.getId() == id){
                    otherUser = u.getUsername();
                }
            }

            String fromOrTo = from ? "To:" : "From:";
            String result = String.format(formatString, t.getTransferId(), fromOrTo, otherUser, "$", t.getAmount());
            strings.add(result);
        }

        return strings.toArray(String[]::new);
    }
}
