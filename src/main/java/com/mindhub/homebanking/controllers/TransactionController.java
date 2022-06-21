package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionService transactionService;


    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(
            @RequestParam double amount, @RequestParam String description,
            @RequestParam String category, @RequestParam String origin,
            @RequestParam String destination, Authentication authentication) {


        Client client = clientService.findByEmail(authentication.getName());
        Account accountOrigin = accountService.findByNumber(origin);
        Account accountDestination = accountService.findByNumber(destination);

        if (amount == 0.0 || description.isEmpty() || origin.isEmpty() || destination.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (origin.equals(destination)) {
            return new ResponseEntity<>("Same account number", HttpStatus.FORBIDDEN);
        }
        if (accountOrigin == null) {
            return new ResponseEntity<>("Account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().contains(accountOrigin)) {
            return new ResponseEntity<>("This is not your account", HttpStatus.FORBIDDEN);
        }
        if (accountDestination == null) {
            return new ResponseEntity<>("Destination account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (amount <= 0) {
            return new ResponseEntity<>("Amount not allowed", HttpStatus.FORBIDDEN);
        }
        if (amount > accountOrigin.getBalance()) {
            return new ResponseEntity<>("Not enough balance to make transaction", HttpStatus.FORBIDDEN);
        }

        double originBalance = accountOrigin.getBalance() - amount;
        double destinationBalance = accountDestination.getBalance() + amount;

        Transaction transactionOrigin = new Transaction(TransactionType.DEBIT, amount * -1,destination + " - " + description, category, LocalDateTime.now(), originBalance, accountOrigin);
        Transaction transactionDestination = new Transaction(TransactionType.CREDIT, amount,origin + " - " + description, category, LocalDateTime.now(), destinationBalance, accountDestination);

        accountOrigin.setBalance(accountOrigin.getBalance() - amount);
        accountDestination.setBalance(accountDestination.getBalance() + amount);

        accountService.saveAccount(accountOrigin);
        accountService.saveAccount(accountDestination);

        transactionService.saveTransaction(transactionOrigin);
        transactionService.saveTransaction(transactionDestination);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}