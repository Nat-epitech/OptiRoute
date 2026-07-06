package com.optiroute.backend.service;

import com.optiroute.backend.dto.request.MissionRequest;
import com.optiroute.backend.entity.Mission;
import com.optiroute.backend.repository.MissionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class MissionService {

    private final MissionRepository missionRepository;

    public MissionService(MissionRepository missionRepository) {
        this.missionRepository = missionRepository;
    }

    @Transactional
    public Mission create(MissionRequest req) {

        Mission m = new Mission();

        m.setName(req.name());
        m.setDriverId(req.driverId());
        m.setVehicleId(req.vehicleId());
        m.setCustomerId(req.customerId());

        m.setPlannedStart(req.plannedStart());
        m.setPlannedEnd(req.plannedEnd());

        m.setOriginName(req.originName());
        m.setOriginAddress(req.originAddress());
        m.setOriginLat(req.originLat());
        m.setOriginLng(req.originLng());

        m.setDestinationName(req.destinationName());
        m.setDestinationAddress(req.destinationAddress());
        m.setDestinationLat(req.destinationLat());
        m.setDestinationLng(req.destinationLng());

        return missionRepository.save(m);
    }
}