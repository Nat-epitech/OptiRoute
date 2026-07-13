package com.optiroute.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.optiroute.backend.entity.Customer;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByCode(String code);

    Optional<Customer> findByExternalId(String externalId);

    boolean existsByCode(String code);
}