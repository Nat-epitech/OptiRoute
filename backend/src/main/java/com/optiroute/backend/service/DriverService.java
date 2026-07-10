package com.optiroute.backend.service;

import com.optiroute.backend.dto.request.DriverRequest;
import com.optiroute.backend.dto.response.DriverResponse;
import com.optiroute.backend.entity.Driver;
import com.optiroute.backend.repository.DriverRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository, PasswordEncoder passwordEncoder) {
        this.driverRepository = driverRepository;
    }

    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    public DriverResponse createDriver(DriverRequest request) {
        Driver driver = new Driver();
        driver.setEmail(request.getEmail());
        driver.setFirstName(request.getFirstName());
        driver.setLastName(request.getLastName());
        driver.setPhoneNumber(request.getPhoneNumber());

        Driver savedDriver = driverRepository.save(driver);

        return DriverResponse.builder().id(savedDriver.getId()).email(savedDriver.getEmail())
                .firstName(savedDriver.getFirstName()).lastName(savedDriver.getLastName()).build();
    }
}