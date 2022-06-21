package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanCreationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.LoanService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private LoanService loanService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/loans")
    public List<LoanDTO> getLoans() {
        return loanService.getLoans();
    }


    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> createLoan(@RequestBody LoanApplicationDTO loanApp, Authentication authentication) {

        Client client = clientService.findByEmail(authentication.getName());
        Loan loan = loanService.findById(loanApp.getId());
        Account account = accountService.findByNumber(loanApp.getNumber());
        double currentBalance = account.getBalance() + loanApp.getAmount();


        if (loanApp.getPayments() == 0 || loanApp.getAmount() == 0 || loanApp.getNumber().isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (loan == null) {
            return new ResponseEntity<>("Loan doesn't exist", HttpStatus.FORBIDDEN);
        }

        if (loanApp.getAmount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("Exceeded max amount", HttpStatus.FORBIDDEN);
        }

        if (!loan.getPayments().contains(loanApp.getPayments())) {
            return new ResponseEntity<>("Payment amount not allowed", HttpStatus.FORBIDDEN);
        }

        if (account == null) {
            return new ResponseEntity<>("Account doesn't exist", HttpStatus.FORBIDDEN);
        }

        if (!client.getAccounts().contains(account)) {
            return new ResponseEntity<>("This account doesn't belong to you", HttpStatus.FORBIDDEN);
        }

        if(client.getLoans().stream().filter(loans -> loans.getId() == loan.getId()).collect(Collectors.toSet()).size()>0){
            return new ResponseEntity<>("Type of loan already taken", HttpStatus.FORBIDDEN);
        }


        ClientLoan clientLoan = new ClientLoan(((loanApp.getAmount() * loan.getPercentage()) / 100) + loanApp.getAmount(), loanApp.getPayments(), client, loan);
        loanService.saveClientLoan(clientLoan);

        Transaction transaction = new Transaction(TransactionType.CREDIT, loanApp.getAmount(), loan.getName() + " Loan approved", "Loan", LocalDateTime.now(), currentBalance , account);
        transactionService.saveTransaction(transaction);

        account.setBalance(account.getBalance() + loanApp.getAmount());
        accountService.saveAccount(account);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/loans/create")
    public ResponseEntity<Object> createLoan(@RequestBody LoanCreationDTO loanCreation) {

        if (loanCreation.getAmount() == 0 || loanCreation.getName().isEmpty() || loanCreation.getPayments().size() == 0 || loanCreation.getPercentage() == 0) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        Loan loan = new Loan(loanCreation.getName(), loanCreation.getAmount(), loanCreation.getPayments(), loanCreation.getPercentage());
        loanService.saveLoan(loan);

        return new ResponseEntity<>("Loan created", HttpStatus.OK);
    }
}
