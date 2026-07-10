package com.optiroute.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.optiroute.backend.dto.request.MissionRequest;
import com.optiroute.backend.entity.Mission;
import com.optiroute.backend.service.MissionFacadeService;

@RestController
@RequestMapping("/api/missions")
public class MissionController {

    private final MissionFacadeService missionFacadeService;

    public MissionController(MissionFacadeService missionFacadeService) {
        this.missionFacadeService = missionFacadeService;
    }

    @PostMapping
    public ResponseEntity<Mission> create(@RequestBody MissionRequest request) {
        Mission mission = missionFacadeService.createMission(request);

        return ResponseEntity.ok(mission);
    }

}