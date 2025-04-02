package com.tw.atdd_workshop.repositories;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tw.atdd_workshop.domains.Card;
import com.tw.atdd_workshop.persistence.StaticDb;

@Service
public class CardRepository {

    public void addCard(Card cardTobeAdded) {
        StaticDb.getCards().add(cardTobeAdded);
    }

    public List<Card> getCustomerCards(String customerId) {
        return StaticDb.getCards().stream().filter(card -> card.getCustomerId() == customerId).toList();
    }
}
