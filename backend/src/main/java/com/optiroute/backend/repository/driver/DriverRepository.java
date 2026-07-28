package com.optiroute.backend.repository.driver;

import org.springframework.data.jpa.repository.JpaRepository;

import com.optiroute.backend.entity.driver.Driver;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    boolean existsByLogin(String login);
}