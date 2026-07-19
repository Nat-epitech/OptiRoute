package com.optiroute.backend.service;

import org.springframework.stereotype.Service;

import com.optiroute.backend.dto.dto.TransportDetailDto;
import com.optiroute.backend.entity.Customer;
import com.optiroute.backend.entity.Driver;
import com.optiroute.backend.entity.Transport;
import com.optiroute.backend.entity.TransportEstimate;
import com.optiroute.backend.entity.Vehicle;
import com.optiroute.backend.repository.CustomerRepository;
import com.optiroute.backend.repository.DriverRepository;
import com.optiroute.backend.repository.TransportRepository;
import com.optiroute.backend.repository.TransportEstimateRepository;
import com.optiroute.backend.repository.VehicleRepository;

@Service
public class TransportDetailService {

    private final TransportRepository transportRepository;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;
    private final TransportEstimateRepository transportEstimateRepository;

    public TransportDetailService(
            TransportRepository transportRepository,
            DriverRepository driverRepository,
            VehicleRepository vehicleRepository,
            CustomerRepository customerRepository,
            TransportEstimateRepository transportEstimateRepository) {

        this.transportRepository = transportRepository;
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
        this.customerRepository = customerRepository;
        this.transportEstimateRepository = transportEstimateRepository;
    }

    public TransportDetailDto getDetail(Long transportId) {

        Transport transport = transportRepository.findById(transportId).orElseThrow(() -> new RuntimeException("Transport not found"));
        Driver driver = driverRepository.findById(transport.getDriverId()).orElseThrow(() -> new RuntimeException("Driver not found"));

        Vehicle vehicle = null;
        if (transport.getVehicleId() != null) {
            vehicle = vehicleRepository.findById(transport.getVehicleId()).orElse(null);
        }

        Customer customer = null;
        if (transport.getCustomerId() != null) {
            customer = customerRepository.findById(transport.getCustomerId()).orElse(null);
        }

        TransportEstimate estimate = transportEstimateRepository.findByTransportId(transportId).orElse(null);

        return new TransportDetailDto(

                transport.getId(),
                transport.getName(),
                transport.getStatus(),

                transport.getPlannedStart(),
                transport.getPlannedEnd(),

                transport.getActualStart(),
                transport.getActualEnd(),

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

                transport.getOriginName(),
                transport.getOriginAddress(),
                transport.getOriginLat(),
                transport.getOriginLng(),

                transport.getDestinationName(),
                transport.getDestinationAddress(),
                transport.getDestinationLat(),
                transport.getDestinationLng(),

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
