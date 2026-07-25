package com.optiroute.backend.repository.vehicle;

import org.springframework.data.jpa.repository.JpaRepository;
import com.optiroute.backend.entity.vehicle.Tractor;

import java.util.Optional;

public interface TractorRepository extends JpaRepository<Tractor, Long> {
    Optional<Tractor> findByRegistration(String registration);

    boolean existsByRegistration(String registration);
}
