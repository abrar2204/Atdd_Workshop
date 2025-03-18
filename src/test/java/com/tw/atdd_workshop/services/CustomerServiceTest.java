package com.tw.atdd_workshop.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tw.atdd_workshop.persistence.StaticDb;

public class CustomerServiceTest {

    private CustomerService service;

    @BeforeEach
    public void init() {
        StaticDb.getCards().clear();
        StaticDb.getCustomers().clear();
        service = new CustomerService();
    }
}