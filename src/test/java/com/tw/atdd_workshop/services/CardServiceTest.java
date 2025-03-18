package com.tw.atdd_workshop.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    
    }

    @Test
    void getCustomerCards_cardsExist_getsAllCustomerCard(){
       
    }
}