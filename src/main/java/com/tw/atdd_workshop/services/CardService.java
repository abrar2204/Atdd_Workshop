package com.tw.atdd_workshop.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.tw.atdd_workshop.domains.*;
import com.tw.atdd_workshop.models.card.CreateCardRequest;
import com.tw.atdd_workshop.persistence.StaticDb;

@Service
public class CardService {

    public Card createCard(CreateCardRequest request){
        Card card = new Card(UUID.randomUUID().toString(), request.customerId(), UUID.randomUUID().toString(), request.cardType());

        StaticDb.getCards().add(card);

        return card;
    }

    public List<Card> getCustomerCards(String customerId){
        return StaticDb.getCards().stream().filter(card -> card.getCustomerId() == customerId).toList();
    }
}