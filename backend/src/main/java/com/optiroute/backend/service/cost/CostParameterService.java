package com.optiroute.backend.service.cost;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.optiroute.backend.entity.cost.CostParameter;
import com.optiroute.backend.repository.cost.CostParameterRepository;
import com.optiroute.backend.type.CostParameterCategoryType;

@Service
public class CostParameterService {

    private final CostParameterRepository costParameterRepository;

    public CostParameterService(CostParameterRepository costParameterRepository) {
        this.costParameterRepository = costParameterRepository;
    }

    public CostParameter getParameter(CostParameterCategoryType category, String label) {
        return costParameterRepository.findByCategoryAndLabel(category,label).orElseThrow(() -> new IllegalStateException("Cost parameter not found: " + category + "/" + label));
    }

    public BigDecimal getValue(CostParameterCategoryType category, String label) {
        return getParameter(category,label).getValue();
    }
}