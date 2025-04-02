package com.tw.atdd_workshop.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tw.atdd_workshop.domains.Customer;
import com.tw.atdd_workshop.models.customer.CreateCustomerRequest;
import com.tw.atdd_workshop.services.CustomerService;

import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

	@GetMapping("/{customerId}")
	public Customer getCustomer(@Parameter @PathVariable("customerId") String customerId) {
		return customerService.getCustomer(customerId);
	}

    @PostMapping("/")
	public Customer createCustomer(@RequestBody CreateCustomerRequest request) {
		return customerService.createCustomer(request);
	}
}
