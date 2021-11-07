package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Scanner;

public class TransferService {

    private String BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;
    private Scanner transferScanner = new Scanner(System.in);


    public TransferService(String url, AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
        BASE_URL = url;
    }


    public void listAllTransfers() {

        Transfer[] allTransfers = null;

        try {
            allTransfers = restTemplate.exchange(BASE_URL + "account/allTransfers", HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();

            for (Transfer transfer : allTransfers) {

                if (currentUser.getUser().getId() == transfer.getSenderId()) {
                    System.out.printf("\n%s %18s %-18s %s%s", transfer.getTransferId(), "To:", transfer.getRecName(),"$", transfer.getAmount());
                } else {
                    System.out.printf("\n%s %18s %-18s %s%s", transfer.getTransferId(), "From:", transfer.getRecName(),"$", transfer.getAmount());
                }
            }

            System.out.println("");
            System.out.print("\nPlease enter transfer ID to view details (0 to cancel): ");
            int transferDetailId = transferScanner.nextInt();

            for (Transfer transfer : allTransfers) {
                if (transfer.getTransferId() == transferDetailId) {
                    System.out.println(transferDetailFormat(transfer));
                }
            }
        } catch (Exception e) {
            System.out.println("Sorry - we have no records for you.");
        }
    }

    public Transfer viewPendingRequests(){

        Transfer[] pendingRequests = restTemplate.exchange(BASE_URL + "account/allTransfers", HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
        Transfer pendingTransfer = null;

        for (Transfer transfer : pendingRequests) {

            if (transfer.getTransferStatusDescription().equals("Pending")) {
                System.out.printf("\n%s %18s %-18s %s%s", transfer.getTransferId(), "To:", transfer.getRecName(),"$", transfer.getAmount());
            }
        }

        System.out.println("");
        System.out.print("\nPlease enter transfer ID to approve/reject (0 to cancel): ");
        int transferDetailId = transferScanner.nextInt();

        for (Transfer transfer : pendingRequests){
            if (transfer.getTransferId() == transferDetailId){
                pendingTransfer = transfer;
            }
        }

        if (transferDetailId == 0){
            System.out.println("");
        }

        return pendingTransfer;
    }

    public void approveOrRejectMenu(Transfer transfer){
        AccountService accountService = new AccountService(BASE_URL, currentUser);

        System.out.println("\n" +
                "\n 1: Approve" +
                "\n 2: Reject" +
                "\n 0: Don't approve or reject" +
                "\n-------------------------------" +
                "\n Please choose an option ");

        int selection = transferScanner.nextInt();

        if (selection == 0){
            System.out.println("Pending transfer status remains unchanged.");

        } else if (selection == 1){
            transfer.setTransferStatusDescription("Approved");

            if (accountService.getBalance().compareTo(transfer.getAmount()) >= 0){
                restTemplate.put(BASE_URL + "account/updateTransfer", makeTransferEntity(transfer), "Approved");
                System.out.println("The transfer was approved.");
            } else {
                System.out.println("Could not complete transfer: Insufficient Funds.");
            }
        } else if (selection == 2){
            transfer.setTransferStatusDescription("Rejected");
            restTemplate.put(BASE_URL + "account/updateTransfer", makeTransferEntity(transfer),"Rejected");
            System.out.println("The transfer was rejected.");
        } else{
            System.out.println("You entered an incorrect option.");
        }

    }

    public void sendBucks () {
        AccountService accountService = new AccountService(BASE_URL, currentUser);
        listUsers();

        Transfer transfer = new Transfer();
        transfer.setSenderId(currentUser.getUser().getId());

        System.out.println("");
        System.out.print("\nEnter ID of user you are sending to (0 to cancel): ");
        transfer.setReceiverId(Integer.parseInt(transferScanner.nextLine()));

        System.out.print("\nEnter amount: ");
        transfer.setAmount(BigDecimal.valueOf(Integer.parseInt(transferScanner.nextLine())));
        System.out.println("");
        // ^^^ console input

        if (accountService.getBalance().compareTo(transfer.getAmount()) >= 0){
            String newTransfer = restTemplate.exchange(BASE_URL + "account/transfer", HttpMethod.POST, makeTransferEntity(transfer), String.class).getBody();
            System.out.println("Transfer was successful!");
        } else {
            System.out.println("Could not complete transfer: Insufficient Funds.");
        }

    }

    public void requestBucks() {

        AccountService accountService = new AccountService(BASE_URL, currentUser);
        listUsers();

        Transfer transfer = new Transfer();
        transfer.setReceiverId(currentUser.getUser().getId());

        System.out.println("");
        System.out.print("\nEnter ID of user you are requesting from (0 to cancel): ");
        transfer.setSenderId(Integer.parseInt(transferScanner.nextLine()));

        System.out.print("\nEnter amount: ");
        transfer.setAmount(BigDecimal.valueOf(Integer.parseInt(transferScanner.nextLine())));
        System.out.println("");
        // ^^^ console input

        String newTransfer = restTemplate.exchange(BASE_URL + "account/transfer/request", HttpMethod.POST, makeTransferEntity(transfer), String.class).getBody();
        System.out.println("Transfer is pending approval");

    }


    private void listUsers () {
        User[] allUsers = restTemplate.exchange(BASE_URL + "account/users", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();

        for (User user : allUsers){
            System.out.printf("\n %s %20s", user.getId(), user.getUsername());
        }
    }

    private HttpEntity makeAuthEntity () {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(transfer, headers);
    }

    public String transferDetailFormat (Transfer transfer){
        return "\n--------------------------------------------" +
                "\n Transfer Details" +
                "\n--------------------------------------------" +
                "\n Id: " + transfer.getTransferId() +
                "\n From: " + transfer.getSenderName() +
                "\n To: " + transfer.getRecName() +
                "\n Type: " + transfer.getTransferTypeDescription() +
                "\n Status: " + transfer.getTransferStatusDescription() +
                "\n Amount: " + transfer.getAmount();
    }

}
