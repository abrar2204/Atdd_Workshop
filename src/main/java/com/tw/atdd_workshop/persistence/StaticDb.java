package com.tw.atdd_workshop.persistence;

import java.util.ArrayList;
import java.util.List;

import com.tw.atdd_workshop.domains.Card;
import com.tw.atdd_workshop.domains.Customer;

public final class StaticDb {
    private static final List<Card> cards = new ArrayList<>();
    private static final List<Customer> customers = new ArrayList<>();

    public static List<Card> getCards(){
        return cards;
    }

    public static List<Customer> getCustomers(){
        return customers;
    }
}

