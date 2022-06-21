package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.Utils.getRandomLong;


@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;


    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountService.getAccounts();
    };

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id, Authentication authentication){
        Client client = clientService.findByEmail(authentication.getName());
        Account account = accountService.findById(id);

        if (!client.getAccounts().contains(account)) {
            return null;
        }
        return new AccountDTO(account);
    };


    @PostMapping(path = "/clients/current/accounts")
    public ResponseEntity<Object> createAccount(@RequestParam AccountType accountType, Authentication authentication) {

        String number = "VIN-" + getRandomLong(1, 99999999);
        Client client = clientService.findByEmail(authentication.getName());
        List<Account> filteredAccounts = client.getAccounts().stream().filter(account -> !account.isDisabled()).collect(Collectors.toList());

        if (filteredAccounts.size() >= 3) {
            return new ResponseEntity<>("Reached account limit", HttpStatus.FORBIDDEN);
        }

        Account account = new Account(number, 0, LocalDateTime.now(), accountType , client, false);
        accountService.saveAccount(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/clients/current/accounts")
    public ResponseEntity<Object> disableAccount(@RequestParam Long id, Authentication authentication){

        Client client = clientService.findByEmail(authentication.getName());
        Account account = accountService.findById(id);

        if (!client.getAccounts().contains(account)) {
            return new ResponseEntity<>("This account doesn't belong to you", HttpStatus.FORBIDDEN);
        }

        account.setDisabled(true);
        accountService.saveAccount(account);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}