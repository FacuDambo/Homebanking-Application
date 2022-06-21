package com.mindhub.homebanking.models;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String number;
    private double balance;
    private LocalDateTime creationDate;
    private boolean disabled;
    private AccountType accountType;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();


    public Account(){}

    public Account(String number, double balance, LocalDateTime creationDate, AccountType accountType , Client cliente, boolean disabled) {
        this.number = number;
        this.balance = balance;
        this.creationDate = creationDate;
        this.disabled = disabled;
        this.client = cliente;
        this.accountType = accountType;
    }


    public long getId() {return id;}

    public String getNumber() {return number;}
    public void setNumber(String number) {this.number = number;}

    public double getBalance() {return balance;}
    public void setBalance(double balance) {this.balance = balance;}

    public LocalDateTime getCreationDate() {return creationDate;}
    public void setCreationDate(LocalDateTime creationDate) {this.creationDate = creationDate;}

    public Client getClient() {return client;}
    public void setClient(Client client) {this.client = client;}

    public boolean isDisabled() {return disabled;}
    public void setDisabled(boolean disabled) {this.disabled = disabled;}

    public AccountType getAccountType() {return accountType;}
    public void setAccountType(AccountType accountType) {this.accountType = accountType;}

    public Set<Transaction> getTransactions() {return transactions;}
    public void addTransactions(Transaction transaction) {
        transaction.setAccount(this);
        transactions.add(transaction);
    }

}
