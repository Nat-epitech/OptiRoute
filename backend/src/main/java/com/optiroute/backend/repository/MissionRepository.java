package com.optiroute.backend.repository;

import com.optiroute.backend.entity.MissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<MissionEntity, Long> {
}