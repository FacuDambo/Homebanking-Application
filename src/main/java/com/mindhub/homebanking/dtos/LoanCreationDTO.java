package com.mindhub.homebanking.dtos;

import java.util.List;

public class LoanCreationDTO {
    private String name;
    private int amount, percentage;
    private List<Integer> payments;

    public LoanCreationDTO() {
    }

    public LoanCreationDTO(String name, int amount, int percentage, List<Integer> payments) {
        this.name = name;
        this.amount = amount;
        this.percentage = percentage;
        this.payments = payments;
    }

    public String getName() {return name;}
    public int getAmount() {return amount;}
    public int getPercentage() {return percentage;}
    public List<Integer> getPayments() {return payments;}
}
