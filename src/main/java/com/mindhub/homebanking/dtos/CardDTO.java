package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CardDTO {
    private long id;
    private String cardHolder, cardName;
    private CardType cardType;
    private CardColor cardColor;
    private long number;
    private int cvv;
    private LocalDate fromDate, thruDate;
    private boolean disabled;

    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardHolder = card.getClient().getFullName();
        this.cardType = card.getCardType();
        this.cardColor = card.getCardColor();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
        this.cardName = card.getCardName();
        this.disabled = card.isDisabled();
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

    public boolean isDisabled() {return disabled;}
    public void setDisabled(boolean disabled) {this.disabled = disabled;}
}
