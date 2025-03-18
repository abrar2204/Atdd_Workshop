package com.tw.atdd_workshop.domains;

public class Card
{
    private final String id;
    private final String customerId;
    private final String cardNumber;
    private final CardType cardType;

    public Card(String id, String customerId, String cardNumber, CardType cardType) {
       this.id = id;
       this.customerId = customerId;
       this.cardNumber = cardNumber;
       this.cardType = cardType;
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public CardType getCardType() {
        return cardType;
    }
} 