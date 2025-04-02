package com.tw.atdd_workshop.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import com.tw.atdd_workshop.domains.Card;
import com.tw.atdd_workshop.domains.CardType;
import com.tw.atdd_workshop.domains.Customer;
import com.tw.atdd_workshop.models.card.CreateCardRequest;
import com.tw.atdd_workshop.persistence.StaticDb;

public class CardServiceTest {

    private CardService service;
    private CustomerService customerService;
    private FeatureToggleService featureToggleService;

    @BeforeEach
    public void init() {
        StaticDb.getCards().clear();
        StaticDb.getCustomers().clear();
        customerService =  mock(CustomerService.class);
        featureToggleService = mock(FeatureToggleService.class);
        service = new CardService(customerService, featureToggleService);
    }

    @Test
    void createCard_validRequest_createsNewCard(){
        // Arrange
        String customerId = UUID.randomUUID().toString();
        Customer customer = new Customer(customerId, "1234567890", true);
        CreateCardRequest request = new CreateCardRequest(customerId, CardType.Visa);
        when(customerService.getCustomer(customerId)).thenReturn(customer);

        // Act
        Card newCard = service.createCard(request);

        // Assert
        List<Card> cards = StaticDb.getCards().stream().filter(c -> c.getCustomerId() == customerId).toList();
        assertEquals(1, cards.size());
        Card actualCard = cards.get(0);
        assertEquals(request.cardType(), actualCard.getCardType());
        assertEquals(newCard.getId(), actualCard.getId());
    }

    @Test
    void createCard_customerDoesNotExist_throwExceptionWithAppropriateMessage(){
        // Arrange
        CreateCardRequest request = new CreateCardRequest(UUID.randomUUID().toString(), CardType.Visa);

        // Act
        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
           () ->service.createCard(request),
           "Expected createdCard() to throw Exception, but it didn't"
        );

        // Assert
        assertTrue(exception.getMessage().contains("Customer Not Found"));
    }

    @Test
    void createCard_customerIsNotVerified_throwExceptionWithAppropriateMessage(){
        // Arrange
        String customerId = UUID.randomUUID().toString();
        Customer customer = new Customer(customerId, "1234567890", false);
        CreateCardRequest request = new CreateCardRequest(customerId, CardType.Visa);
        when(customerService.getCustomer(customerId)).thenReturn(customer);

        // Act
        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
           () ->service.createCard(request),
           "Expected createdCard() to throw Exception, but it didn't"
        );

        // Assert
        assertTrue(exception.getMessage().contains("Customer Not Verified"));
    }

    @Test
    void createCard_checkCustomerRefactoredFeatureToggleIsOn_useNewImplementationFromCustomerServiceAndCreateCard()
    {
        // Arrange
        String customerId = UUID.randomUUID().toString();
        when(featureToggleService.IsOn("check-customer-refactored")).thenReturn(true);
        CreateCardRequest request = new CreateCardRequest(customerId, CardType.Visa);

        // Act
        Card newCard = service.createCard(request);

        // Assert
        verify(customerService, times(1)).checkCustomer(customerId);
        List<Card> cards = StaticDb.getCards().stream().filter(c -> c.getCustomerId() == customerId).toList();
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
        assertTrue(cards.stream().allMatch(card -> card.getId() == card1.getId() || card.getId() == card2.getId() || card.getId() == card3.getId())); 
        assertTrue(cards.stream().noneMatch(card -> card.getId() == card4.getId() || card.getId() == card5.getId())); 
    }
}