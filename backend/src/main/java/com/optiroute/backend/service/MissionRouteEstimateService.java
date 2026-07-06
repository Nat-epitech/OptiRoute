package com.optiroute.backend.service;

import com.optiroute.backend.repository.MissionRouteEstimateRepository;
import com.optiroute.backend.entity.MissionRouteEstimate;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class MissionRouteEstimateService {

    private final MissionRouteEstimateRepository repository;

    public MissionRouteEstimateService(MissionRouteEstimateRepository repository) {
        this.repository = repository;
    }

    public MissionRouteEstimate save(MissionRouteEstimate estimate) {
        return repository.save(estimate);
    }

    public Optional<MissionRouteEstimate> findByMissionId(Long missionId) {
        return repository.findByMissionId(missionId);
    }
}