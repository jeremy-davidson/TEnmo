package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
        String url = API_BASE_URL + "account";
        HttpEntity entity = createEntityWithToken(currentUser.getToken());

        ResponseEntity<Account> response = restTemplate.exchange(url, HttpMethod.GET, entity, Account.class);

        BigDecimal bal = response.getBody().getBalance();
        String result = String.format("Your current account balance is: $%.2f", bal);
        System.out.println(result);
    }

    private void viewTransferHistory() {
        String url = API_BASE_URL;
        HttpEntity entity = createEntityWithToken(currentUser.getToken());

        Transfer[] historyItems = restTemplate.exchange(url + "transfer", HttpMethod.GET, entity, Transfer[].class).getBody();

        String[] strs = TransferService.formatTransferStrings(historyItems, currentUser.getUser());
        consoleService.printTransferArray(strs);

        String prompt = "Please enter transfer ID to view details (0 to cancel): ";
        int userInput = consoleService.promptForInt(prompt);
        if(userInput <= 0){
            return;
        }

        //get specific transfer
        url = url + "transfer/" + userInput;
        Transfer transferRequested = restTemplate.exchange(url, HttpMethod.GET, entity, Transfer.class).getBody();
        if(transferRequested != null){
            //Print out transfer requested
            consoleService.printTransferDetails(transferRequested);
        } else{
            System.out.println("Transfer not available.");
        }

    }

    private void viewPendingRequests() {
        // TODO Auto-generated method stub

    }

    private void sendBucks() {
        String url = API_BASE_URL + "user";
        HttpEntity entity = createEntityWithToken(currentUser.getToken());

        ResponseEntity<User[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, User[].class);

        List<User> users = removeCurrentUserFromList(response.getBody());

        //show list of users
        consoleService.printSendMenu(users.toArray(User[]::new));

        //prompt for user selection
        String promptForID = "\nEnter ID of user you are sending to (0 to cancel): ";
        long selectedUser = currentUser.getUser().getId();
        while (selectedUser == currentUser.getUser().getId()) {

            selectedUser = consoleService.promptForInt(promptForID);

            if (selectedUser <= 0) {
                return;
            } else if (selectedUser == currentUser.getUser().getId()) {
                String complaint = "\nYou can't send money to yourself!\n";
                System.out.println(complaint);
            }
        }

        //prompt for dollar amount
        String promptForAmount = "Enter amount: ";
        BigDecimal amount = new BigDecimal(0.00);
        while (amount.doubleValue() <= 0) {
            amount = consoleService.promptForBigDecimal(promptForAmount);
            amount = amount.setScale(2, RoundingMode.DOWN);

            if (amount.doubleValue() <= 0) {
                String complaint = "\nYou can't send nothing or a negative amount!\n";
                System.out.println(complaint);
            }
        }

        // Build Transfer Object
        Transfer transfer = new Transfer();
        transfer.setTransferType(2);
        transfer.setTransferStatus(2);
        transfer.setAccountFrom(currentUser.getUser().getId());
        transfer.setAccountTo(selectedUser);
        transfer.setAmount(amount);

        //post request
        url = API_BASE_URL + "transfer";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity<Transfer> transferHttpEntity = new HttpEntity<>(transfer, headers);
        Boolean result = false;
        try {
            result = restTemplate.postForObject(url, transferHttpEntity, Boolean.class);
        } catch (Exception e){

        }

        //print result message
        if (result) {
            System.out.println("Money Sent!");
        } else {
            System.out.println("Something went wrong! Please make sure your inputs are correct.");
        }
    }

    private void requestBucks() {
        // TODO Auto-generated method stub

    }

    private HttpEntity createEntityWithToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity(headers);
    }

    private List<User> removeCurrentUserFromList(User[] array) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            if (!array[i].getId().equals(currentUser.getUser().getId())) {
                users.add(array[i]);
            }
        }

        return users;
    }

}
