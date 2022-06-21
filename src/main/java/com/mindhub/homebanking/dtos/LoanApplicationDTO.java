package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {
    private long id;
    private double amount;
    private int payments;
    private String number;

    public LoanApplicationDTO() {}

    public LoanApplicationDTO(long id, int amount, int payment, String number){
        this.id = id;
        this.amount = amount;
        this.payments = payment;
        this.number = number;
    }

    public long getId() {return id;}
    public double getAmount() {return amount;}
    public int getPayments() {return payments;}
    public String getNumber() {return number;}
}
