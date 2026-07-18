package com.optiroute.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.optiroute.backend.dto.request.CustomerRequest;
import com.optiroute.backend.dto.response.CustomerResponse;
import com.optiroute.backend.entity.Customer;
import com.optiroute.backend.repository.CustomerRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public CustomerResponse create(CustomerRequest request) {

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
    public List<CustomerResponse> getAll() {
        return customerRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public CustomerResponse getById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with id: " + id));

        return toDto(customer);
    }

    @Transactional
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id " + id));

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

        Customer updatedCustomer = customerRepository.save(customer);
        return toDto(updatedCustomer);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new EntityNotFoundException("Customer not found with id " + id);
        }

        customerRepository.deleteById(id);
    }

    private CustomerResponse toDto(Customer customer) {
        return new CustomerResponse(
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