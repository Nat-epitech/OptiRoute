package com.optiroute.backend.controller.vehicle;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.optiroute.backend.dto.request.vehicle.TractorRequest;
import com.optiroute.backend.dto.response.vehicle.TractorResponse;
import com.optiroute.backend.dto.response.vehicle.TractorLightResponse;
import com.optiroute.backend.service.vehicle.TractorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tractors")
public class TractorController {

    private final TractorService tractorService;

    public TractorController(TractorService tractorService) {
        this.tractorService = tractorService;
    }

    @PostMapping
    public ResponseEntity<TractorResponse> create(@RequestBody TractorRequest request) {
        TractorResponse createdTractor = tractorService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTractor);
    }

    @GetMapping
    public ResponseEntity<List<TractorLightResponse>> getAll() {
        return ResponseEntity.ok(tractorService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TractorResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(tractorService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTractor(@PathVariable Long id) {
        tractorService.deleteTractor(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TractorResponse> updateTractor(@PathVariable Long id, @Valid @RequestBody TractorRequest request) {
        TractorResponse updatedTractor = tractorService.updateTractor(id, request);

        return ResponseEntity.ok(updatedTractor);
    }
}