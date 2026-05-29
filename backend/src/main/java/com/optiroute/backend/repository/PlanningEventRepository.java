package com.optiroute.backend.repository;

import com.optiroute.backend.entity.PlanningEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanningEventRepository extends JpaRepository<PlanningEvent, Long> {
    List<PlanningEvent> findByDriverId(Long driverId);
}