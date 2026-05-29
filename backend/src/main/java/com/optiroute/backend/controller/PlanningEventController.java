package com.optiroute.backend.controller;

import com.optiroute.backend.dto.request.PlanningEventRequest;
import com.optiroute.backend.dto.response.PlanningEventResponse;
import com.optiroute.backend.entity.PlanningEvent;
import com.optiroute.backend.service.PlanningEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planning-events")
@RequiredArgsConstructor
public class PlanningEventController {

    private final PlanningEventService planningEventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody PlanningEventRequest request) {
        planningEventService.create(request);
    }

    public interface PlanningEventRepository extends JpaRepository<PlanningEvent, Long> {
        List<PlanningEvent> findByDriverId(Long driverId);
    }

    @GetMapping("/driver/{driverId}")
    public List<PlanningEventResponse> getByDriver(@PathVariable Long driverId) {
        return planningEventService.getByDriver(driverId);
    }
}