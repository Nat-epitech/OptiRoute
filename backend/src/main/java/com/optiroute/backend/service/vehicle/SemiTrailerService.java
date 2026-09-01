package com.optiroute.backend.service.vehicle;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.optiroute.backend.dto.request.vehicle.SemiTrailerRequest;
import com.optiroute.backend.dto.response.vehicle.SemiTrailerResponse;
import com.optiroute.backend.dto.response.vehicle.SemiTrailerLightResponse;
import com.optiroute.backend.entity.vehicle.SemiTrailer;
import com.optiroute.backend.repository.vehicle.SemiTrailerRepository;
import com.optiroute.backend.utils.VehicleUtils;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SemiTrailerService {

    private final SemiTrailerRepository semiTrailerRepository;

    public SemiTrailerService(SemiTrailerRepository semiTrailerRepository) {
        this.semiTrailerRepository = semiTrailerRepository;
    }

    public List<SemiTrailerLightResponse> getAll() {
        return semiTrailerRepository.findAll().stream().map(semiTrailer -> VehicleUtils.toSemiTrailerLightResponse(semiTrailer)).toList();
    }

    @Transactional(readOnly = true)
    public SemiTrailerResponse getById(Long id) {
        SemiTrailer semiTrailer = semiTrailerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("SemiTrailer not found with id: " + id));

        return VehicleUtils.toSemiTrailerResponse(semiTrailer);
    }

    public SemiTrailer getEntityById(Long id) {
        return semiTrailerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("SemiTrailer not found with id: " + id));
    }

    @Transactional
    public SemiTrailerResponse create(SemiTrailerRequest request) {

        if (semiTrailerRepository.existsByRegistration(request.registration())) {
            throw new IllegalArgumentException("A semiTrailer with registration " + request.registration() + " already exists");
        }

        SemiTrailer semiTrailer = new SemiTrailer();

        semiTrailer.setExternalId(request.externalId());
        semiTrailer.setExternalSource(request.externalSource() == null || request.externalSource().isBlank() ? "MANUAL" : request.externalSource());

        semiTrailer.setRegistration(request.registration());

        semiTrailer.setBrand(request.brand());
        semiTrailer.setModel(request.model());

        semiTrailer.setTrailerType(request.trailerType());
        semiTrailer.setMaxSpeed(request.maxSpeed());

        semiTrailer.setEmptyWeightKg(request.emptyWeightKg());
        semiTrailer.setGrossVehicleWeightKg(request.grossVehicleWeightKg());

        semiTrailer.setHeightCm(request.heightCm());
        semiTrailer.setWidthCm(request.widthCm());
        semiTrailer.setLengthCm(request.lengthCm());
        semiTrailer.setAxleCount(request.axleCount());

        semiTrailer.setPurchaseCost(request.purchaseCost());
        semiTrailer.setDepreciationStartDate(request.depreciationStartDate());
        semiTrailer.setDepreciationEndDate(request.depreciationEndDate());

        SemiTrailer savedSemiTrailer = semiTrailerRepository.save(semiTrailer);
        return VehicleUtils.toSemiTrailerResponse(savedSemiTrailer);
    }

    @Transactional
    public SemiTrailerResponse updateSemiTrailer(Long id, SemiTrailerRequest request) {
        SemiTrailer semiTrailer = semiTrailerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("SemiTrailer not found with id " + id));

        semiTrailer.setExternalId(request.externalId());
        semiTrailer.setExternalSource(request.externalSource() == null || request.externalSource().isBlank() ? "MANUAL" : request.externalSource());

        semiTrailer.setRegistration(request.registration());

        semiTrailer.setBrand(request.brand());
        semiTrailer.setModel(request.model());

        semiTrailer.setTrailerType(request.trailerType());
        semiTrailer.setMaxSpeed(request.maxSpeed());

        semiTrailer.setEmptyWeightKg(request.emptyWeightKg());
        semiTrailer.setGrossVehicleWeightKg(request.grossVehicleWeightKg());

        semiTrailer.setHeightCm(request.heightCm());
        semiTrailer.setWidthCm(request.widthCm());
        semiTrailer.setLengthCm(request.lengthCm());
        semiTrailer.setAxleCount(request.axleCount());

        semiTrailer.setPurchaseCost(request.purchaseCost());
        semiTrailer.setDepreciationStartDate(request.depreciationStartDate());
        semiTrailer.setDepreciationEndDate(request.depreciationEndDate());

        SemiTrailer updatedSemiTrailer = semiTrailerRepository.save(semiTrailer);
        return VehicleUtils.toSemiTrailerResponse(updatedSemiTrailer);
    }

    @Transactional
    public void deleteSemiTrailer(Long id) {
        if (!semiTrailerRepository.existsById(id)) {
            throw new EntityNotFoundException("SemiTrailer not found with id " + id);
        }

        semiTrailerRepository.deleteById(id);
    }

}