package com.tw.atdd_workshop.services;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.tw.atdd_workshop.domains.*;
import com.tw.atdd_workshop.models.card.CreateCardRequest;
import com.tw.atdd_workshop.persistence.StaticDb;
import com.tw.atdd_workshop.repositories.CardRepository;

@Service
public class CardService {

    private final CustomerService customerService;
    private final FeatureToggleService featureToggleService;
    private final CardRepository cardRepository;

    public CardService(CustomerService customerService, FeatureToggleService featureToggleService, CardRepository cardRepository) {
        this.customerService = customerService;
        this.featureToggleService = featureToggleService;
        this.cardRepository = cardRepository;
    }

    public Card createCard(CreateCardRequest request){
        if(featureToggleService.IsOn("check-customer-refactored")){
            customerService.checkCustomer(request.customerId());
        }else{
            Customer customer = customerService.getCustomer(request.customerId());

            if(customer == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer Not Found");
            }

            if(!customer.getIsVerified()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer Not Verified");
            }
        }

        Card existingCard = StaticDb.getCards().stream().filter(card -> card.getCustomerId().equals(request.customerId()) && card.getCardType().equals(request.cardType())).findFirst().orElse(null);
        if(existingCard != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card Already Ordered");
        }
 
        Card card = new Card(UUID.randomUUID().toString(), request.customerId(), UUID.randomUUID().toString(), request.cardType());

        if(featureToggleService.IsOn("card-repository-refactored")){
            cardRepository.addCard(card);
        }else{
            StaticDb.getCards().add(card);
        }

        return card;
    }

    public List<Card> getCustomerCards(String customerId){
        if(featureToggleService.IsOn("card-repository-refactored")){
            return cardRepository.getCustomerCards(customerId);
        }else{
            return StaticDb.getCards().stream().filter(card -> card.getCustomerId().equals(customerId)).toList();
        }
    }
}