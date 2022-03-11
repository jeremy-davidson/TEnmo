package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferHistoryItem;
import com.techelevator.tenmo.model.User;

import java.util.ArrayList;
import java.util.List;

public class TransferService {

    public static String[] formatTransferStrings(TransferHistoryItem[] historyItems, User currentUser) {
        List<String> strings = new ArrayList<>();
        String formatString = "%1d%13s%12s%12s%7.2f%n";

        for (TransferHistoryItem i: historyItems) {
            boolean from = i.getUsernameFrom().equals(currentUser.getUsername());
            String otherUser = from ? i.getUsernameTo() : i.getUsernameFrom() ;
            String fromOrTo = from ? "To:" : "From:";
            String result = String.format(formatString, i.getTransferId(), fromOrTo, otherUser, "$", i.getAmount());
            strings.add(result);
        }

        return strings.toArray(String[]::new);
    }
}
