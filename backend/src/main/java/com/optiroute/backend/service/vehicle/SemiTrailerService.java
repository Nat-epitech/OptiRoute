package com.optiroute.backend.service.vehicle;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.optiroute.backend.dto.request.vehicle.SemiTrailerRequest;
import com.optiroute.backend.dto.response.vehicle.SemiTrailerResponse;
import com.optiroute.backend.entity.vehicle.SemiTrailer;
import com.optiroute.backend.repository.vehicle.SemiTrailerRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SemiTrailerService {

    private final SemiTrailerRepository semiTrailerRepository;

    public SemiTrailerService(SemiTrailerRepository semiTrailerRepository) {
        this.semiTrailerRepository = semiTrailerRepository;
    }

    @Transactional(readOnly = true)
    public List<SemiTrailerResponse> getAll() {
        return semiTrailerRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public SemiTrailerResponse getById(Long id) {
        SemiTrailer semiTrailer = semiTrailerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("SemiTrailer not found with id: " + id));

        return toDto(semiTrailer);
    }

    @Transactional
    public SemiTrailerResponse create(SemiTrailerRequest request) {

        if (semiTrailerRepository.existsByRegistration(request.registration())) {
            throw new IllegalArgumentException("A semiTrailer with registration " + request.registration() + " already exists");
        }

        SemiTrailer semiTrailer = new SemiTrailer();

        semiTrailer.setExternalId(request.externalId());
        semiTrailer.setExternalSource(request.externalSource() == null || request.externalSource().isBlank()
                ? "MANUAL"
                : request.externalSource());

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

        semiTrailer.setActive(true);

        SemiTrailer savedSemiTrailer = semiTrailerRepository.save(semiTrailer);
        return toDto(savedSemiTrailer);
    }

    @Transactional
    public SemiTrailerResponse updateSemiTrailer(Long id, SemiTrailerRequest request) {
        SemiTrailer semiTrailer = semiTrailerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SemiTrailer not found with id " + id));

        semiTrailer.setExternalId(request.externalId());
        semiTrailer.setExternalSource(request.externalSource() == null || request.externalSource().isBlank()
                ? "MANUAL"
                : request.externalSource());

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

        semiTrailer.setActive(true);

        SemiTrailer updatedSemiTrailer = semiTrailerRepository.save(semiTrailer);
        return toDto(updatedSemiTrailer);
    }

    @Transactional
    public void deleteSemiTrailer(Long id) {
        if (!semiTrailerRepository.existsById(id)) {
            throw new EntityNotFoundException("SemiTrailer not found with id " + id);
        }

        semiTrailerRepository.deleteById(id);
    }

    private SemiTrailerResponse toDto(SemiTrailer semiTrailer) {
        return new SemiTrailerResponse(
                semiTrailer.getId(),
                semiTrailer.getExternalId(),
                semiTrailer.getExternalSource(),
                semiTrailer.getRegistration(),
                semiTrailer.getBrand(),
                semiTrailer.getModel(),
                semiTrailer.getTrailerType(),
                semiTrailer.getMaxSpeed(),
                semiTrailer.getEmptyWeightKg(),
                semiTrailer.getGrossVehicleWeightKg(),
                semiTrailer.getHeightCm(),
                semiTrailer.getWidthCm(),
                semiTrailer.getLengthCm(),
                semiTrailer.getAxleCount(),
                semiTrailer.getPurchaseCost(),
                semiTrailer.getDepreciationStartDate(),
                semiTrailer.getDepreciationEndDate(),
                semiTrailer.getActive(),
                semiTrailer.getCreatedAt(),
                semiTrailer.getUpdatedAt());
    }
}