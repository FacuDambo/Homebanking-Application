package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String name;
    private int maxAmount;
    private int percentage;

    @ElementCollection
    @Column(name="payments")
    private List<Integer> payments = new ArrayList<>();

    @OneToMany(mappedBy="loan", fetch=FetchType.EAGER)
    private Set<ClientLoan> clientsLoans = new HashSet<>();


    public Loan() {}

    public Loan(String name, int maxAmount, List<Integer> payments, int percentage) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.percentage = percentage;
    }

    public long getId() {return id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public int getMaxAmount() {return maxAmount;}
    public void setMaxAmount(int maxAmount) {this.maxAmount = maxAmount;}

    public List<Integer> getPayments() {return payments;}
    public void setPayments(List<Integer> payments) {this.payments = payments;}

    public int getPercentage() {return percentage;}
    public void setPercentage(int percentage) {this.percentage = percentage;}

    public Set<ClientLoan> getClientsLoans() {return clientsLoans;}
    public void addClientLoans(ClientLoan clientLoan){
        clientLoan.setLoan(this);
        clientsLoans.add(clientLoan);
    }

    public List<Client> getClients() {
        return clientsLoans.stream().map(loan -> loan.getClient()).collect(Collectors.toList());
    }
}
