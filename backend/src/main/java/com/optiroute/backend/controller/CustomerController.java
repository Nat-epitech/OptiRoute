package com.optiroute.backend.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.optiroute.backend.dto.dto.CustomerDto;
import com.optiroute.backend.dto.request.CustomerRequest;
import com.optiroute.backend.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerDto> create(
            @RequestBody CustomerRequest request) {

        CustomerDto createdCustomer = customerService.create(request);

        return ResponseEntity.created(URI.create("/api/customers/" + createdCustomer.id())).body(createdCustomer);
    }

    @GetMapping({ "", "/" })
    public List<CustomerDto> getAll() {
        return customerService.getAll();
    }

    @GetMapping("/{id}")
    public CustomerDto getById(@PathVariable Long id) {
        return customerService.getById(id);
    }
}