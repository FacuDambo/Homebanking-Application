package com.mindhub.homebanking.dtos;

public class PaymentApplicationDTO {


    private long numberCard;

    private long cvv;

    private double amount;

    private String description;

    public PaymentApplicationDTO() {
    }

    public PaymentApplicationDTO(long numberCard, long cvv, double amount, String description) {
        this.numberCard = numberCard;
        this.cvv = cvv;
        this.amount = amount;
        this.description = description;
    }

    public long getNumberCard() {
        return numberCard;
    }

    public long getCvv() {
        return cvv;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
