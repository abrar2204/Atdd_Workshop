package com.tw.atdd_workshop.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tw.atdd_workshop.domains.Card;
import com.tw.atdd_workshop.domains.CardType;
import com.tw.atdd_workshop.persistence.StaticDb;

public class CardRepositoryTests {
    private CardRepository repository;

    @BeforeEach
    public void init() {
        StaticDb.getCards().clear();
        repository = new CardRepository();
    }

    @Test
    void addCard_validCard_addsNewCard(){
        // Arrange
        Card cardTobeAdded = new Card(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), CardType.Visa);

        // Act
        repository.addCard(cardTobeAdded);

        // Assert
        List<Card> cards = StaticDb.getCards().stream().filter(c -> c.getId() == cardTobeAdded.getId()).toList();
        assertEquals(1, cards.size());
        Card actualCard = cards.get(0);
        assertEquals(cardTobeAdded.getCardType(), actualCard.getCardType());
        assertEquals(cardTobeAdded.getId(), actualCard.getId());
    }

    @Test
    void getCustomerCards_cardsExists_returnsCards(){
        // Arrange
        String customerId = UUID.randomUUID().toString();
        Card card1 = new Card(UUID.randomUUID().toString(), customerId, UUID.randomUUID().toString(), CardType.Visa);
        Card card2 = new Card(UUID.randomUUID().toString(), customerId, UUID.randomUUID().toString(), CardType.MasterCard);
        Card card3 = new Card(UUID.randomUUID().toString(), customerId, UUID.randomUUID().toString(), CardType.RuPay);
        Card card4 = new Card(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), CardType.RuPay);
        Card card5 = new Card(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), CardType.RuPay);
        StaticDb.getCards().addAll(Arrays.asList(card1, card2, card3, card4, card5));

        // Act
        List<Card> cards = repository.getCustomerCards(customerId);

        // Assert
        assertEquals(3, cards.size());
        assertTrue(cards.stream().allMatch(card -> card.getId() == card1.getId() || card.getId() == card2.getId() || card.getId() == card3.getId())); 
        assertTrue(cards.stream().noneMatch(card -> card.getId() == card4.getId() || card.getId() == card5.getId())); 
    }
}
