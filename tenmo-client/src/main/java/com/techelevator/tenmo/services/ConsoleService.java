package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;

import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printSendMenu(User[] users){
        printBreakLine();
        System.out.println("Users");
        System.out.printf("ID%14s%n","NAME");
        printBreakLine();
        for(User u:users){
            System.out.printf("%-12d%s%n",u.getId(),u.getUsername());
        }
        System.out.println("---------");
    }

    public void printTransferArray(String[] strings){
        printBreakLine();
        System.out.println("Transfers");
        System.out.printf("ID%17s%23s%n","From/To", "Amount");
        printBreakLine();
        for(String str:strings){
            System.out.printf(str);
        }
        System.out.println("---------");
    }

    public void printTransferDetails(Transfer t) {
        printBreakLine();
        System.out.println("Transfer Details");
        printBreakLine();
        System.out.println("Id: " + t.getTransferId());
        System.out.println("From: " + t.getUserNameFrom());
        System.out.println("To: " + t.getUserNameTo());
        System.out.println("Type: " + TransferService.translateType(t.getTransferType()));
        System.out.println("Status: " + TransferService.translateStatus(t.getTransferStatus()));
        System.out.println("Amount: $" + t.getAmount());
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void printBreakLine(){
        System.out.println("-------------------------------------------");
    }

}
