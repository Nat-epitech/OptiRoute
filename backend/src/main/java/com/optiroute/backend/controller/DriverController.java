package com.optiroute.backend.controller;

import com.optiroute.backend.dto.request.DriverRequest;
import com.optiroute.backend.dto.response.DriverResponse;
import com.optiroute.backend.entity.DriverEntity;
import com.optiroute.backend.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping
    public List<DriverEntity> getDrivers() {
        return driverService.getAllDrivers();
    }

    @PostMapping
    public DriverResponse createDriver(@Valid @RequestBody DriverRequest request) {
        return driverService.createDriver(request);
    }
}