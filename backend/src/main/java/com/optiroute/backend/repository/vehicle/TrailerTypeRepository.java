package com.optiroute.backend.repository.vehicle;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.optiroute.backend.entity.vehicle.TrailerType;

public interface TrailerTypeRepository extends JpaRepository<TrailerType, Long> {
    Optional<TrailerType> findByLabelIgnoreCase(String label);
}
