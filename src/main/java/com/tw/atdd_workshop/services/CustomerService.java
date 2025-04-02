package com.tw.atdd_workshop.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.tw.atdd_workshop.domains.Customer;
import com.tw.atdd_workshop.models.customer.CreateCustomerRequest;
import com.tw.atdd_workshop.persistence.StaticDb;

@Service
public class CustomerService {

    public Customer createCustomer(CreateCustomerRequest request){
        Customer customer = new Customer(UUID.randomUUID().toString(), request.phoneNumber(), true);

        StaticDb.getCustomers().add(customer);

        return customer;
    }

    public Customer getCustomer(String customerId){
        return StaticDb.getCustomers().stream().filter(customer -> customer.getId().equals(customerId)).findFirst().orElse(null);
    }
}
