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
        BASE_URL = url + "/account";
    }


    public void listAllTransfers() {

        Transfer[] allTransfers = null;

        try {
            allTransfers = restTemplate.exchange(BASE_URL + "/allTransfers", HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();

            for (Transfer transfer : allTransfers) {

                if (currentUser.getUser().getId() == transfer.getSenderId()) {
                    System.out.printf("\n %s %20s %s %20s", transfer.getTransferId(), "To:", transfer.getRecName(), transfer.getAmount());
                } else {
                    System.out.printf("\n %s %20s %s %20s", transfer.getTransferId(), "From:", transfer.getRecName(), transfer.getAmount());
                }
            }

            System.out.println("");
            System.out.print("Please enter transfer ID to view details (0 to cancel): ");
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

    private void listUsers () {
        User[] allUsers = restTemplate.exchange(BASE_URL + "/users", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();

        for (User user : allUsers){
            System.out.printf("\n %s %20s", user.getId(), user.getUsername());
        }
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

    public Transfer sendBucks () {
        listUsers();

        System.out.println("");
        System.out.print("\n Enter ID of user you are sending to (0 to cancel): ");
        int receiverId = transferScanner.nextInt();

        System.out.print("\n Enter amount: ");
        int money = transferScanner.nextInt();
        System.out.println("");

        // ^^^ console input

        BigDecimal transferAmount = new BigDecimal(money);

        Transfer transfer = new Transfer(currentUser.getUser().getId(), receiverId, transferAmount);

        Transfer newTransfer = restTemplate.postForObject(BASE_URL + "/transfer", makeTransferEntity(transfer), Transfer.class);

        return newTransfer;

    }
}
