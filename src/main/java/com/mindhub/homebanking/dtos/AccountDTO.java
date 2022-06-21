package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {
    private long id;
    private String number;
    private double balance;
    private boolean disabled;
    private AccountType accountType;
    private LocalDateTime creationDate;
    private Set<TransactionDTO> transaction = new HashSet<>();

    public AccountDTO(Account account){
        this.id = account.getId();
        this.number = account.getNumber();
        this.balance = account.getBalance();
        this.disabled = account.isDisabled();
        this.accountType = account.getAccountType();
        this.creationDate = account.getCreationDate();
        this.transaction = account.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toSet());
    }

    public long getId() {return id;}

    public String getNumber() {return number;}
    public void setNumber(String number) {this.number = number;}

    public double getBalance() {return balance;}
    public void setBalance(double balance) {this.balance = balance;}

    public boolean isDisabled() {return disabled;}
    public void setDisabled(boolean disabled) {this.disabled = disabled;}

    public AccountType getAccountType() {return accountType;}
    public void setAccountType(AccountType accountType) {this.accountType = accountType;}

    public LocalDateTime getCreationDate() {return creationDate;}
    public void setCreationDate(LocalDateTime creationDate) {this.creationDate = creationDate;}

    public Set<TransactionDTO> getTransaction() {return transaction;}
}
