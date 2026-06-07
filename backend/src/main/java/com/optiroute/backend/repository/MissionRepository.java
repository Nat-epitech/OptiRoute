package com.optiroute.backend.repository;

import com.optiroute.backend.entity.MissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissionRepository extends JpaRepository<MissionEntity, Long> {
    List<MissionEntity> findByDriverId(Long driverId);
}