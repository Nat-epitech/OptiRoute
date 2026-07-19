package com.optiroute.backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.optiroute.backend.dto.dto.TransportDetailDto;
import com.optiroute.backend.dto.dto.TransportPlanningDto;
import com.optiroute.backend.dto.request.TransportRequest;
import com.optiroute.backend.dto.request.CreateTransportFromRouteRequest;
import com.optiroute.backend.entity.Transport;
import com.optiroute.backend.service.TransportDetailService;
import com.optiroute.backend.service.TransportFacadeService;
import com.optiroute.backend.service.TransportPlanningService;

@RestController
@RequestMapping("/api/transports")
public class TransportController {

    private final TransportFacadeService transportFacadeService;
    private final TransportPlanningService transportPlanningService;
    private final TransportDetailService transportDetailService;

    public TransportController(TransportFacadeService transportFacadeService, TransportPlanningService transportPlanningService,
            TransportDetailService transportDetailService) {
        this.transportFacadeService = transportFacadeService;
        this.transportPlanningService = transportPlanningService;
        this.transportDetailService = transportDetailService;
    }

    @PostMapping
    public ResponseEntity<Transport> create(@RequestBody TransportRequest request) {
        Transport transport = transportFacadeService.createTransport(request);

        return ResponseEntity.ok(transport);
    }

    @GetMapping("/planning")
    public ResponseEntity<List<TransportPlanningDto>> getPlanning(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(transportPlanningService.getPlanning(startDate, endDate));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransportDetailDto> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(transportDetailService.getDetail(id));
    }

    @PostMapping("/from-route")
    public ResponseEntity<Transport> createFromRoute(@RequestBody CreateTransportFromRouteRequest request) {
        Transport transport = transportFacadeService.createFromRoute(request);

        return ResponseEntity.ok(transport);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransport(@PathVariable Long id) {
        transportFacadeService.deleteTransport(id);

        return ResponseEntity.noContent().build();
    }

}