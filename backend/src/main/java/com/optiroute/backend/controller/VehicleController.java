package com.optiroute.backend.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.optiroute.backend.dto.dto.VehicleDto;
import com.optiroute.backend.dto.request.VehicleRequest;
import com.optiroute.backend.service.VehicleService;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public ResponseEntity<VehicleDto> create(@RequestBody VehicleRequest request) {
        VehicleDto createdVehicle = vehicleService.create(request);

        return ResponseEntity.created(URI.create("/api/vehicles/" + createdVehicle.id())).body(createdVehicle);
    }

    @GetMapping
    public List<VehicleDto> getAll() {
        return vehicleService.getAll();
    }

    @GetMapping("/{id}")
    public VehicleDto getById(@PathVariable Long id) {
        return vehicleService.getById(id);
    }
}