package com.optiroute.backend.service;

import com.optiroute.backend.dto.request.MissionRequest;
import com.optiroute.backend.dto.response.MissionResponse;
import com.optiroute.backend.entity.DriverEntity;
import com.optiroute.backend.entity.MissionEntity;
import com.optiroute.backend.repository.DriverRepository;
import com.optiroute.backend.repository.MissionRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final DriverRepository driverRepository;

    public MissionEntity create(MissionRequest request) {

        DriverEntity driver = driverRepository.findById(request.driverId()).orElseThrow(() -> new RuntimeException("Driver not found"));

        if (request.endDatetime().isBefore(request.startDatetime())) {
            throw new IllegalArgumentException("End datetime must be after start datetime");
        }

        MissionEntity event = new MissionEntity();

        event.setDriver(driver);
        event.setTitle(request.title());
        event.setStartDatetime(request.startDatetime());
        event.setEndDatetime(request.endDatetime());
        event.setStatus("PLANNED");
        event.setCreatedAt(LocalDateTime.now());
        event.setUpdatedAt(LocalDateTime.now());

        return missionRepository.save(event);
    }

    public List<MissionResponse> getByDriver(Long driverId) {

        return missionRepository.findByDriverId(driverId)
                .stream()
                .map(e -> new MissionResponse(
                        e.getId(),
                        e.getTitle(),
                        e.getStartDatetime(),
                        e.getEndDatetime()))
                .toList();
    }
}