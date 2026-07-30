package com.optiroute.backend.repository.transport;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.optiroute.backend.entity.transport.TransportEstimate;

public interface TransportEstimateRepository extends JpaRepository<TransportEstimate, Long> {
    Optional<TransportEstimate> findByTransportId(Long transportId);
}