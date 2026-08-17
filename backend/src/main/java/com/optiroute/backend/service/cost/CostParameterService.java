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

    public CostParameter getParameter(CostParameterCategoryType category, String parameter) {

        return costParameterRepository.findByCategoryAndParameter(category,parameter)
            .orElseThrow(() -> new IllegalStateException("Cost parameter not found: " + category + "/" + parameter));
    }

    public BigDecimal getValue(CostParameterCategoryType category, String parameter) {
        return getParameter(category,parameter).getValue();
    }
}