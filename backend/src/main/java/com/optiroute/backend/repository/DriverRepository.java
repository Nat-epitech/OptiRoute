package com.optiroute.backend.repository;

import com.optiroute.backend.entity.DriverEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<DriverEntity, Long> {
    Optional<DriverEntity> findByEmail(String email);
}