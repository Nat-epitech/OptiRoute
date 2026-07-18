package com.optiroute.backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.optiroute.backend.dto.dto.MissionDetailDto;
import com.optiroute.backend.dto.dto.MissionPlanningDto;
import com.optiroute.backend.dto.request.MissionRequest;
import com.optiroute.backend.dto.request.CreateMissionFromRouteRequest;
import com.optiroute.backend.entity.Mission;
import com.optiroute.backend.service.MissionDetailService;
import com.optiroute.backend.service.MissionFacadeService;
import com.optiroute.backend.service.MissionPlanningService;

@RestController
@RequestMapping("/api/missions")
public class MissionController {

    private final MissionFacadeService missionFacadeService;
    private final MissionPlanningService missionPlanningService;
    private final MissionDetailService missionDetailService;

    public MissionController(MissionFacadeService missionFacadeService, MissionPlanningService missionPlanningService,
            MissionDetailService missionDetailService) {
        this.missionFacadeService = missionFacadeService;
        this.missionPlanningService = missionPlanningService;
        this.missionDetailService = missionDetailService;
    }

    @PostMapping
    public ResponseEntity<Mission> create(@RequestBody MissionRequest request) {
        Mission mission = missionFacadeService.createMission(request);

        return ResponseEntity.ok(mission);
    }

    @GetMapping("/planning")
    public ResponseEntity<List<MissionPlanningDto>> getPlanning(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(missionPlanningService.getPlanning(startDate, endDate));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MissionDetailDto> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(missionDetailService.getDetail(id));
    }

    @PostMapping("/from-route")
    public ResponseEntity<Mission> createFromRoute(@RequestBody CreateMissionFromRouteRequest request) {
        Mission mission = missionFacadeService.createFromRoute(request);

        return ResponseEntity.ok(mission);
    }

}