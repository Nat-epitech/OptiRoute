package com.optiroute.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.optiroute.backend.entity.Vehicle;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByRegistration(String registration);

    boolean existsByRegistration(String registration);
}