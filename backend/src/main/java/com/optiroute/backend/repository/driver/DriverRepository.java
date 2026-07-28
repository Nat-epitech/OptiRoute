package com.optiroute.backend.repository.driver;

import org.springframework.data.jpa.repository.JpaRepository;

import com.optiroute.backend.entity.driver.Driver;

import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByEmail(String email);
}