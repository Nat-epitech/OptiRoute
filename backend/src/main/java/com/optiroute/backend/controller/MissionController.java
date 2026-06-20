package com.optiroute.backend.controller;

import com.optiroute.backend.dto.request.MissionRequest;
import com.optiroute.backend.dto.response.MissionResponse;
import com.optiroute.backend.entity.MissionEntity;
import com.optiroute.backend.service.MissionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/missions")
public class MissionController {

    private final MissionService missionService;

    public MissionController(MissionService missionService) {
        this.missionService = missionService;
    }

    @PostMapping
    public MissionResponse create(@RequestBody MissionRequest request) {
        MissionEntity mission = missionService.create(request);
        return missionService.getById(mission.getId());
    }

    @GetMapping("/driver/{driverId}")
    public List<MissionResponse> getByDriver(@PathVariable Long driverId) {
        return missionService.getByDriver(driverId);
    }

    @GetMapping("/{missionId}")
    public MissionResponse getById(@PathVariable Long missionId) {
        return missionService.getById(missionId);
    }
}