package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String cardHolder, cardName;
    private CardType cardType;
    private CardColor cardColor;
    private int cvv;
    private long number;
    private LocalDate fromDate, thruDate;
    private boolean disabled;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    public Card() {}

    public Card(CardType cardType, CardColor cardColor, long number, int cvv, LocalDate fromDate, LocalDate thruDate, String cardName, Client client, boolean disabled) {
        this.cardHolder = client.getFullName();
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.cardName = cardName;
        this.client = client;
        this.disabled = disabled;
    }

    public long getId() {return id;}

    public String getCardHolder() {return cardHolder;}
    public void setCardHolder(String cardHolder) {this.cardHolder = cardHolder;}

    public CardType getCardType() {return cardType;}
    public void setCardType(CardType cardType) {this.cardType = cardType;}

    public CardColor getCardColor() {return cardColor;}
    public void setCardColor(CardColor cardColor) {this.cardColor = cardColor;}

    public long getNumber() {return number;}
    public void setNumber(long number) {this.number = number;}

    public int getCvv() {return cvv;}
    public void setCvv(int cvv) {this.cvv = cvv;}

    public LocalDate getFromDate() {return fromDate;}
    public void setFromDate(LocalDate fromDate) {this.fromDate = fromDate;}

    public LocalDate getThruDate() {return thruDate;}
    public void setThruDate(LocalDate thruDate) {this.thruDate = thruDate;}

    public String getCardName() {return cardName;}
    public void setCardName(String cardName) {this.cardName = cardName;}

    public Client getClient() {return client;}
    public void setClient(Client client) {this.client = client;}

    public boolean isDisabled() {return disabled;}
    public void setDisabled(boolean disabled) {this.disabled = disabled;}
}
