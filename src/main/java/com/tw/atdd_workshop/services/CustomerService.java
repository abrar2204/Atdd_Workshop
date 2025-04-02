package com.tw.atdd_workshop.services;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.tw.atdd_workshop.domains.Customer;
import com.tw.atdd_workshop.models.customer.CreateCustomerRequest;
import com.tw.atdd_workshop.persistence.StaticDb;

@Service
public class CustomerService {

    public Customer createCustomer(CreateCustomerRequest request){
        Customer customer = new Customer(UUID.randomUUID().toString(), request.phoneNumber(), false);

        StaticDb.getCustomers().add(customer);

        return customer;
    }

    public Customer getCustomer(String customerId){
        return StaticDb.getCustomers().stream().filter(customer -> customer.getId().equals(customerId)).findFirst().orElse(null);
    }

    public void checkCustomer(String customerId) {
        Customer customer = getCustomer(customerId);

        if(customer == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer Not Found");
        }

        if(!customer.getIsVerified()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer Not Verified");
        }
    }
}
