package com.optiroute.backend.repository.transport;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.optiroute.backend.entity.transport.Transport;

public interface TransportRepository extends JpaRepository<Transport, Long> {
    List<Transport> findByPlannedStartGreaterThanEqualAndPlannedStartLessThan(OffsetDateTime start, OffsetDateTime end);

    List<Transport> findByTractorIdAndPlannedStartGreaterThanEqualAndPlannedStartLessThan(Long tractorId, OffsetDateTime start, OffsetDateTime end);

    List<Transport> findByDriverIdAndPlannedStartGreaterThanEqualAndPlannedStartLessThan(Long driverId, OffsetDateTime start, OffsetDateTime end);
}