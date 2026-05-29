package com.optiroute.backend.service;

import com.optiroute.backend.dto.request.PlanningEventRequest;
import com.optiroute.backend.dto.response.PlanningEventResponse;
import com.optiroute.backend.entity.DriverEntity;
import com.optiroute.backend.entity.PlanningEvent;
import com.optiroute.backend.repository.DriverRepository;
import com.optiroute.backend.repository.PlanningEventRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlanningEventService {

    private final PlanningEventRepository planningEventRepository;
    private final DriverRepository driverRepository;

    public PlanningEvent create(PlanningEventRequest request) {

        DriverEntity driver = driverRepository.findById(request.driverId()).orElseThrow(() -> new RuntimeException("Driver not found"));

        if (request.endDatetime().isBefore(request.startDatetime())) {
            throw new IllegalArgumentException("End datetime must be after start datetime");
        }

        PlanningEvent event = new PlanningEvent();

        event.setDriver(driver);
        event.setTitle(request.title());
        event.setStartDatetime(request.startDatetime());
        event.setEndDatetime(request.endDatetime());
        event.setStatus("PLANNED");
        event.setCreatedAt(LocalDateTime.now());
        event.setUpdatedAt(LocalDateTime.now());

        return planningEventRepository.save(event);
    }

    public List<PlanningEventResponse> getByDriver(Long driverId) {

        return planningEventRepository.findByDriverId(driverId)
                .stream()
                .map(e -> new PlanningEventResponse(
                        e.getId(),
                        e.getTitle(),
                        e.getStartDatetime(),
                        e.getEndDatetime()))
                .toList();
    }
}