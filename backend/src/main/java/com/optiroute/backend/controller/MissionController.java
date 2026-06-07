package com.optiroute.backend.controller;

import com.optiroute.backend.dto.request.MissionRequest;
import com.optiroute.backend.dto.response.MissionResponse;
import com.optiroute.backend.entity.MissionEntity;
import com.optiroute.backend.service.MissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody MissionRequest request) {
        missionService.create(request);
    }

    public interface MissionRepository extends JpaRepository<MissionEntity, Long> {
        List<MissionEntity> findByDriverId(Long driverId);
    }

    @GetMapping("/driver/{driverId}")
    public List<MissionResponse> getByDriver(@PathVariable Long driverId) {
        return missionService.getByDriver(driverId);
    }
}