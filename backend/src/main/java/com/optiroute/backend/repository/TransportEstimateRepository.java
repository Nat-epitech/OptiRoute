package com.optiroute.backend.repository;

import com.optiroute.backend.entity.TransportEstimate;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransportEstimateRepository extends JpaRepository<TransportEstimate, Long> {
    Optional<TransportEstimate> findByTransportId(Long transportId);
}