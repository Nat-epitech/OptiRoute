package com.optiroute.backend.controller;

import com.optiroute.backend.dto.request.MissionRequest;
import com.optiroute.backend.entity.MissionEntity;
import com.optiroute.backend.service.MissionService;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/missions")
public class MissionController {

    private final MissionService missionService;

    public MissionController(MissionService missionService) {
        this.missionService = missionService;
    }

    @PostMapping
    public ResponseEntity<MissionEntity> create(@RequestBody MissionRequest request) {
        MissionEntity created = missionService.create(request);
        return ResponseEntity.ok(created);
    }
}