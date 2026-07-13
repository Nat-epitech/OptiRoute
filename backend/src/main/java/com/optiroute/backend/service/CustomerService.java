package com.optiroute.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.optiroute.backend.dto.dto.CustomerDto;
import com.optiroute.backend.dto.request.CustomerRequest;
import com.optiroute.backend.entity.Customer;
import com.optiroute.backend.repository.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public CustomerDto create(CustomerRequest request) {

        if (request.name() == null || request.name().isBlank()) {
            throw new IllegalArgumentException("Customer name is required");
        }

        if (request.code() != null && !request.code().isBlank() && customerRepository.existsByCode(request.code())) {
            throw new IllegalArgumentException("A customer with code " + request.code() + " already exists");
        }

        Customer customer = new Customer();

        customer.setExternalId(request.externalId());
        customer.setExternalSource(request.externalSource() == null || request.externalSource().isBlank()
                ? "MANUAL"
                : request.externalSource());

        customer.setName(request.name());
        customer.setCode(request.code());
        customer.setAddress(request.address());
        customer.setCity(request.city());
        customer.setCountry(request.country());
        customer.setMetadata(request.metadata());

        return toDto(customerRepository.save(customer));
    }

    @Transactional(readOnly = true)
    public List<CustomerDto> getAll() {
        return customerRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public CustomerDto getById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with id: " + id));

        return toDto(customer);
    }

    private CustomerDto toDto(Customer customer) {
        return new CustomerDto(
                customer.getId(),
                customer.getExternalId(),
                customer.getExternalSource(),
                customer.getName(),
                customer.getCode(),
                customer.getAddress(),
                customer.getCity(),
                customer.getCountry(),
                customer.getMetadata(),
                customer.getCreatedAt(),
                customer.getUpdatedAt());
    }
}