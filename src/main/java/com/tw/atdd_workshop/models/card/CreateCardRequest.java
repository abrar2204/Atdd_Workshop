package com.tw.atdd_workshop.models.card;

import com.tw.atdd_workshop.domains.CardType;

public record CreateCardRequest(String customerId, CardType cardType) {}
