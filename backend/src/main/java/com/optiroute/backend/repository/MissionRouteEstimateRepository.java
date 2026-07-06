package com.optiroute.backend.repository;

import com.optiroute.backend.entity.MissionRouteEstimate;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRouteEstimateRepository extends JpaRepository<MissionRouteEstimate, Long> {
    Optional<MissionRouteEstimate> findByMissionId(Long missionId);
}