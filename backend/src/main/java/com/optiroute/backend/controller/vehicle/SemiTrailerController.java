package com.optiroute.backend.controller.vehicle;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.optiroute.backend.dto.request.vehicle.SemiTrailerRequest;
import com.optiroute.backend.dto.response.vehicle.SemiTrailerResponse;
import com.optiroute.backend.dto.response.vehicle.SemiTrailerLightResponse;

import com.optiroute.backend.service.vehicle.SemiTrailerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/semiTrailers")
public class SemiTrailerController {

    private final SemiTrailerService semiTrailerService;

    public SemiTrailerController(SemiTrailerService semiTrailerService) {
        this.semiTrailerService = semiTrailerService;
    }

    @PostMapping
    public ResponseEntity<SemiTrailerResponse> create(@RequestBody SemiTrailerRequest request) {
        SemiTrailerResponse createdSemiTrailer = semiTrailerService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdSemiTrailer);
    }

    @GetMapping
    public ResponseEntity<List<SemiTrailerLightResponse>> getAll() {
        return ResponseEntity.ok(semiTrailerService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SemiTrailerResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(semiTrailerService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSemiTrailer(@PathVariable Long id) {
        semiTrailerService.deleteSemiTrailer(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<SemiTrailerResponse> updateSemiTrailer(@PathVariable Long id, @Valid @RequestBody SemiTrailerRequest request) {
        SemiTrailerResponse updatedSemiTrailer = semiTrailerService.updateSemiTrailer(id, request);

        return ResponseEntity.ok(updatedSemiTrailer);
    }
}