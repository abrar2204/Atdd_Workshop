package com.tw.atdd_workshop.services;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.tw.atdd_workshop.domains.*;
import com.tw.atdd_workshop.models.card.CreateCardRequest;
import com.tw.atdd_workshop.persistence.StaticDb;

@Service
public class CardService {

    private final CustomerService customerService;

    public CardService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public Card createCard(CreateCardRequest request){

        Customer customer = customerService.getCustomer(request.customerId());

        if(customer == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer Not Found");
        }

        if(!customer.getIsVerified()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer Not Verified");
        }

        Card card = new Card(UUID.randomUUID().toString(), request.customerId(), UUID.randomUUID().toString(), request.cardType());

        StaticDb.getCards().add(card);

        return card;
    }

    public List<Card> getCustomerCards(String customerId){
        return StaticDb.getCards().stream().filter(card -> card.getCustomerId().equals(customerId)).toList();
    }
}