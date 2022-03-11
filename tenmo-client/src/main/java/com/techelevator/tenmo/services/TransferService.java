package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.util.ArrayList;
import java.util.List;

public class TransferService {

    public static String[] formatTransferStrings(Transfer[] historyItems, User currentUser) {
        List<String> strings = new ArrayList<>();

        String formatString = "%-12d%-5s %-17s $%8.2f%n";

        for (Transfer i: historyItems) {
            boolean from = i.getUserNameFrom().equals(currentUser.getUsername());
            String otherUser = from ? i.getUserNameTo() : i.getUserNameFrom() ;
            String fromOrTo = from ? "To:" : "From:";
            String result = String.format(formatString, i.getTransferId(), fromOrTo, otherUser, i.getAmount());
            strings.add(result);
        }

        return strings.toArray(String[]::new);
    }

    public static String translateStatus(int statusId){
        switch(statusId){
            case 1: return "Pending";
            case 2: return "Approved";
            case 3: return "Rejected";
            default:return "Unknown";
        }
    }

    public static String translateType(int typeId){
        switch (typeId){
            case 1: return "Request";
            case 2: return "Send";
            default: return "Unknown";
        }
    }
}
