package com.optiroute.backend.mapper;

import com.optiroute.backend.dto.response.RouteDto;
import com.optiroute.backend.entity.RouteEntity;
import org.springframework.stereotype.Component;

@Component
public class RouteMapper {

    public static RouteDto toDto(RouteEntity entity) {

        if (entity == null) {
            return null;
        }

        RouteDto dto = new RouteDto();

        dto.setId(entity.getId());

        dto.setDistanceKm(entity.getDistanceKm());
        dto.setDurationMinutes(entity.getDurationMinutes());

        dto.setFuelCost(entity.getFuelCost());
        dto.setTollCost(entity.getTollCost());
        dto.setDriverCost(entity.getDriverCost());

        dto.setPolyline(entity.getPolyline());

        dto.setActions(entity.getActions());

        dto.setFuelCost(entity.getFuelCost());
        dto.setTollCost(entity.getTollCost());
        dto.setDriverCost(entity.getDriverCost());

        return dto;
    }
}