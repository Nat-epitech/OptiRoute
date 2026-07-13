package com.optiroute.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.optiroute.backend.dto.request.VehicleRequest;
import com.optiroute.backend.dto.response.VehicleResponse;
import com.optiroute.backend.entity.Vehicle;
import com.optiroute.backend.repository.VehicleRepository;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    public VehicleResponse create(VehicleRequest request) {

        if (vehicleRepository.existsByRegistration(request.registration())) {
            throw new IllegalArgumentException(
                    "A vehicle with registration "
                            + request.registration()
                            + " already exists");
        }

        Vehicle vehicle = new Vehicle();

        vehicle.setExternalId(request.externalId());

        vehicle.setExternalSource(
                request.externalSource() == null
                        || request.externalSource().isBlank()
                                ? "MANUAL"
                                : request.externalSource());

        vehicle.setRegistration(request.registration());
        vehicle.setBrand(request.brand());
        vehicle.setModel(request.model());
        vehicle.setFuelType(request.fuelType());
        vehicle.setAverageConsumption(request.averageConsumption());
        vehicle.setTankCapacity(request.tankCapacity());
        vehicle.setMetadata(request.metadata());

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return toDto(savedVehicle);
    }

    @Transactional(readOnly = true)
    public List<VehicleResponse> getAll() {
        return vehicleRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public VehicleResponse getById(Long id) {

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Vehicle not found with id: " + id));

        return toDto(vehicle);
    }

    private VehicleResponse toDto(Vehicle vehicle) {
        return new VehicleResponse(
                vehicle.getId(),
                vehicle.getExternalId(),
                vehicle.getExternalSource(),
                vehicle.getRegistration(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getFuelType(),
                vehicle.getAverageConsumption(),
                vehicle.getTankCapacity(),
                vehicle.getMetadata(),
                vehicle.getCreatedAt(),
                vehicle.getUpdatedAt());
    }
}