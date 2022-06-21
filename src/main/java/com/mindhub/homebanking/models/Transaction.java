package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private TransactionType type;
    private double amount;
    private String descripcion;

    private String category;
    private LocalDateTime creationDate;

    private double balance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;


    public Transaction() {}

    public Transaction(TransactionType type, double amount, String descripcion, String category, LocalDateTime creationDate, double balance , Account account) {
        this.type = type;
        this.amount = amount;
        this.descripcion = descripcion;
        this.category = category;
        this.creationDate = creationDate;
        this.account = account;
        this.balance = balance;
    }

    public long getId() {return id;}

    public TransactionType getType() {return type;}
    //public void setType(TransactionType type) {this.type = type;}

    public double getAmount() {return amount;}
    public void setAmount(double amount) {this.amount = amount;}

    public String getCategory() {return category;}
    public void setCategory(String category) {this.category = category;}

    public String getDescripcion() {return descripcion;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}

    public LocalDateTime getCreationDate() {return creationDate;}
    public void setCreationDate(LocalDateTime creationDate) {this.creationDate = creationDate;}

    public Account getAccount() {return account;}
    public void setAccount(Account account) {this.account = account;}

    public double getBalance() {return balance;}
    public void setBalance(double balance) {this.balance = balance;}
}
