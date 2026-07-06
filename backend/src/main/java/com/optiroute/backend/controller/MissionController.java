package com.optiroute.backend.controller;

import com.optiroute.backend.dto.request.MissionRequest;
import com.optiroute.backend.entity.Mission;
import com.optiroute.backend.service.MissionFacadeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/missions")
public class MissionController {

    private final MissionFacadeService facadeService;

    public MissionController(MissionFacadeService facadeService) {
        this.facadeService = facadeService;
    }

    @PostMapping
    public Mission create(@RequestBody MissionRequest request) {
        return facadeService.createMission(request);
    }
}