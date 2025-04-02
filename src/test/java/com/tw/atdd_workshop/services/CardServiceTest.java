package com.tw.atdd_workshop.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tw.atdd_workshop.domains.Card;
import com.tw.atdd_workshop.domains.CardType;
import com.tw.atdd_workshop.models.card.CreateCardRequest;
import com.tw.atdd_workshop.persistence.StaticDb;

public class CardServiceTest {

    private CardService service;

    @BeforeEach
    public void init() {
        StaticDb.getCards().clear();
        StaticDb.getCustomers().clear();
        service = new CardService();
    }

    @Test
    void createCard_validRequest_createsNewCard(){
        // Arrange
        String customerId = UUID.randomUUID().toString();
        CreateCardRequest request = new CreateCardRequest(customerId, CardType.Visa);

        // Act
        Card newCard = service.createCard(request);

        // Assert
        List<Card> cards = StaticDb.getCards().stream().filter(c -> c.getCustomerId().equals(customerId)).toList();
        assertEquals(1, cards.size());
        Card actualCard = cards.get(0);
        assertEquals(request.cardType(), actualCard.getCardType());
        assertEquals(newCard.getId(), actualCard.getId());
    }

    @Test
    void getCustomerCards_cardsExist_getsAllCustomerCard(){
        // Arrange
        String customerId = UUID.randomUUID().toString();
        Card card1 = new Card(UUID.randomUUID().toString(), customerId, UUID.randomUUID().toString(), CardType.Visa);
        Card card2 = new Card(UUID.randomUUID().toString(), customerId, UUID.randomUUID().toString(), CardType.MasterCard);
        Card card3 = new Card(UUID.randomUUID().toString(), customerId, UUID.randomUUID().toString(), CardType.RuPay);
        Card card4 = new Card(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), CardType.RuPay);
        Card card5 = new Card(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), CardType.RuPay);
        StaticDb.getCards().addAll(Arrays.asList(card1, card2, card3, card4, card5));

        // Act
        List<Card> cards = service.getCustomerCards(customerId);

        // Assert
        assertEquals(3, cards.size());
        assertTrue(cards.stream().allMatch(card -> card.getId().equals(card1.getId()) || card.getId().equals(card2.getId()) || card.getId().equals(card3.getId()))); 
        assertTrue(cards.stream().noneMatch(card -> card.getId().equals(card4.getId()) || card.getId().equals(card5.getId()))); 
    }
}