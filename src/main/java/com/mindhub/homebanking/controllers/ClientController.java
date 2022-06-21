package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.utils.Utils.getRandomInteger;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;


    @GetMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientService.getClients();
    };

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return clientService.getClient(id);
    };

    @GetMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication) {
        return clientService.getCurrentClient(authentication);
    }


    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (clientService.findByEmail(email) != null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        String number = "VIN-" + getRandomInteger(1, 99999999);
        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        Account account = new Account(number, 0, LocalDateTime.now(), AccountType.SAVING , client, false);

        accountService.saveAccount(account);
        clientService.saveClient(client);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}