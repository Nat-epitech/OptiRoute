package com.optiroute.backend.repository;

import com.optiroute.backend.entity.Transport;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransportRepository extends JpaRepository<Transport, Long> {
    List<Transport> findByPlannedStartGreaterThanEqualAndPlannedStartLessThan(OffsetDateTime start, OffsetDateTime end);
}