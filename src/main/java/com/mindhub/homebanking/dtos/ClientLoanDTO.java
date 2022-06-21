package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class ClientLoanDTO {
    private long id;
    private long idLoan;
    private String name;
    private int payments;
    private double amount;

    public ClientLoanDTO() {}
    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.idLoan = clientLoan.getLoan().getId();
        this.name = clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
    }

    public long getId() {return id;}

    public double getAmount() {return amount;}
    public void setAmount(double amount) {this.amount = amount;}

    public int getPayments() {return payments;}
    public void setPayments(int payments) {this.payments = payments;}

    public long getIdLoan() {return idLoan;}
    public void setIdLoan(long idLoan) {this.idLoan = idLoan;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
}
