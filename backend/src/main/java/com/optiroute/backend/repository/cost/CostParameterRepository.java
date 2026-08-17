package com.optiroute.backend.repository.cost;

import org.springframework.data.jpa.repository.JpaRepository;

import com.optiroute.backend.entity.cost.CostParameter;
import com.optiroute.backend.type.CostParameterCategoryType;

import java.util.Optional;

public interface CostParameterRepository extends JpaRepository<CostParameter, Long> {
    Optional<CostParameter> findByCategoryAndLabel(CostParameterCategoryType category, String label);
}