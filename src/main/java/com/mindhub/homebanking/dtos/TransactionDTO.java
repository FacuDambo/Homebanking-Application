package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDateTime;

public class TransactionDTO {
    private long id;
    private TransactionType type;
    private double amount;
    private String descripcion;
    private String category;
    private double balance;
    private LocalDateTime creationDate;

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.descripcion = transaction.getDescripcion();
        this.category = transaction.getCategory();
        this.creationDate = transaction.getCreationDate();
        this.balance = transaction.getBalance();
    }

    public long getId() {return id;}

    public TransactionType getType() {return type;}
    public void setType(TransactionType type) {this.type = type;}

    public double getAmount() {return amount;}
    public void setAmount(double amount) {this.amount = amount;}

    public String getDescripcion() {return descripcion;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}

    public String getCategory() {return category;}
    public void setCategory(String category) {this.category = category;}

    public LocalDateTime getCreationDate() {return creationDate;}
    public void setCreationDate(LocalDateTime creationDate) {this.creationDate = creationDate;}

    public double getBalance() {return balance;}
    public void setBalance(double balance) {this.balance = balance;}
}
