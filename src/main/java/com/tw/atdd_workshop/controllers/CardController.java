package com.tw.atdd_workshop.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tw.atdd_workshop.domains.Card;
import com.tw.atdd_workshop.models.card.CreateCardRequest;
import com.tw.atdd_workshop.services.CardService;

import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/card")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

	@GetMapping("/{customerId}")
	public List<Card> getCustomerCards(@Parameter @PathVariable("customerId") String customerId) {
		return cardService.getCustomerCards(customerId);
	}

    @PostMapping("/")
	public Card createCard(@RequestBody CreateCardRequest request) {
		return cardService.createCard(request);
	}
}
