package com.optiroute.backend.repository;

import com.optiroute.backend.entity.Mission;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    List<Mission> findByPlannedStartGreaterThanEqualAndPlannedStartLessThan(OffsetDateTime start, OffsetDateTime end);
}