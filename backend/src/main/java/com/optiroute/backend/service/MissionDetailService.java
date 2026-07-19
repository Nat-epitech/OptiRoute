package com.optiroute.backend.service;

import org.springframework.stereotype.Service;

import com.optiroute.backend.dto.dto.MissionDetailDto;
import com.optiroute.backend.entity.Customer;
import com.optiroute.backend.entity.Driver;
import com.optiroute.backend.entity.Mission;
import com.optiroute.backend.entity.MissionRouteEstimate;
import com.optiroute.backend.entity.Vehicle;
import com.optiroute.backend.repository.CustomerRepository;
import com.optiroute.backend.repository.DriverRepository;
import com.optiroute.backend.repository.MissionRepository;
import com.optiroute.backend.repository.MissionRouteEstimateRepository;
import com.optiroute.backend.repository.VehicleRepository;

@Service
public class MissionDetailService {

    private final MissionRepository missionRepository;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;
    private final MissionRouteEstimateRepository estimateRepository;

    public MissionDetailService(
            MissionRepository missionRepository,
            DriverRepository driverRepository,
            VehicleRepository vehicleRepository,
            CustomerRepository customerRepository,
            MissionRouteEstimateRepository estimateRepository) {

        this.missionRepository = missionRepository;
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
        this.customerRepository = customerRepository;
        this.estimateRepository = estimateRepository;
    }

    public MissionDetailDto getDetail(Long missionId) {

        Mission mission = missionRepository.findById(missionId).orElseThrow(() -> new RuntimeException("Mission not found"));
        Driver driver = driverRepository.findById(mission.getDriverId()).orElseThrow(() -> new RuntimeException("Driver not found"));

        Vehicle vehicle = null;
        if (mission.getVehicleId() != null) {
            vehicle = vehicleRepository.findById(mission.getVehicleId()).orElse(null);
        }

        Customer customer = null;
        if (mission.getCustomerId() != null) {
            customer = customerRepository.findById(mission.getCustomerId()).orElse(null);
        }

        MissionRouteEstimate estimate = estimateRepository.findByMissionId(missionId).orElse(null);

        return new MissionDetailDto(

                mission.getId(),
                mission.getName(),
                mission.getStatus(),

                mission.getPlannedStart(),
                mission.getPlannedEnd(),

                mission.getActualStart(),
                mission.getActualEnd(),

                driver.getId(),
                driver.getFirstName() + " " + driver.getLastName(),
                driver.getEmail(),

                vehicle != null ? vehicle.getId() : null,
                vehicle != null ? vehicle.getRegistration() : null,
                vehicle != null ? vehicle.getBrand() : null,
                vehicle != null ? vehicle.getModel() : null,

                customer != null ? customer.getId() : null,
                customer != null ? customer.getName() : null,
                customer != null ? customer.getAddress() : null,
                customer != null ? customer.getCity() : null,

                mission.getOriginName(),
                mission.getOriginAddress(),
                mission.getOriginLat(),
                mission.getOriginLng(),

                mission.getDestinationName(),
                mission.getDestinationAddress(),
                mission.getDestinationLat(),
                mission.getDestinationLng(),

                estimate != null ? estimate.getDistanceMeters() : null,
                estimate != null ? estimate.getDurationSeconds() : null,
                estimate != null ? estimate.getTrafficDurationSeconds() : null,

                estimate != null ? estimate.getPolyline() : null,

                estimate != null ? estimate.getEstimatedFuelCost() : null,
                estimate != null ? estimate.getEstimatedTollCost() : null,
                estimate != null ? estimate.getEstimatedDriverCost() : null,
                estimate != null ? estimate.getEstimatedTotalCost() : null);
    }
}
