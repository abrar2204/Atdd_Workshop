package com.tw.atdd_workshop.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import com.tw.atdd_workshop.domains.Customer;
import com.tw.atdd_workshop.models.customer.CreateCustomerRequest;
import com.tw.atdd_workshop.persistence.StaticDb;

public class CustomerServiceTest {

    private CustomerService service;

    @BeforeEach
    public void init() {
        StaticDb.getCards().clear();
        StaticDb.getCustomers().clear();
        service = new CustomerService();
    }


    @Test
    void createCustomer_validRequest_createsNewCustomer(){
        // Arrange
        String phoneNumber = UUID.randomUUID().toString();
        CreateCustomerRequest request = new CreateCustomerRequest(phoneNumber);

        // Act
        Customer newCustomer = service.createCustomer(request);

        // Assert
        List<Customer> customers = StaticDb.getCustomers().stream().filter(c -> c.getPhoneNumber() == phoneNumber).toList();
        assertEquals(1, customers.size());
        Customer actualCustomer = customers.get(0);
        assertEquals(request.phoneNumber(), actualCustomer.getPhoneNumber());
        assertEquals(newCustomer.getId(), actualCustomer.getId());
    }

    @Test
    void getCustomers_customerExist_getsCustomer(){
        // Arrange
        Customer expectedCustomer = new Customer(UUID.randomUUID().toString(), UUID.randomUUID().toString(), false);
        StaticDb.getCustomers().add(expectedCustomer);

        // Act
        Customer actualCustomer = service.getCustomer(expectedCustomer.getId());

        // Assert
        assertEquals(expectedCustomer, actualCustomer);
    }

    @Test
    void checkCustomer_customerDoesNotExist_throwsException(){
        // Arrange
        String customerId = UUID.randomUUID().toString();

        // Act
        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
           () ->service.checkCustomer(customerId),
           "Expected checkCustomer() to throw Exception, but it didn't"
        );

        // Assert
        assertTrue(exception.getMessage().contains("Customer Not Found"));
    }

    @Test
    void checkCustomer_customerIsNotVerified_throwsException(){
        // Arrange
        Customer expectedCustomer = new Customer(UUID.randomUUID().toString(), UUID.randomUUID().toString(), false);
        StaticDb.getCustomers().add(expectedCustomer);

        // Act
        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
           () ->service.checkCustomer(expectedCustomer.getId()),
           "Expected checkCustomer() to throw Exception, but it didn't"
        );

        // Assert
        assertTrue(exception.getMessage().contains("Customer Not Verified"));
    }
}