package com.optiroute.backend.service.driver;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.optiroute.backend.dto.request.driver.DriverRequest;
import com.optiroute.backend.dto.response.driver.DriverResponse;
import com.optiroute.backend.dto.response.driver.DriverSummaryResponse;
import com.optiroute.backend.entity.driver.Driver;
import com.optiroute.backend.repository.driver.DriverRepository;

import com.optiroute.backend.utils.DriverUtils;

import java.util.List;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository, PasswordEncoder passwordEncoder) {
        this.driverRepository = driverRepository;
    }

    public List<DriverSummaryResponse> getAll() {
        return driverRepository.findAll()
                .stream()
                .map(driver -> DriverUtils.toSummaryResponse(driver))
                .toList();
    }

    @Transactional(readOnly = true)
    public DriverResponse getById(Long id) {
        Driver driver = driverRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Driver not found with id: " + id));

        return DriverUtils.toDriverResponse(driver);
    }

    @Transactional
    public DriverResponse createDriver(DriverRequest request) {
        Driver driver = new Driver();
        driver.setLogin(DriverUtils.generateUniqueLogin(driverRepository, request.getFirstName(), request.getLastName()));
        driver.setFirstName(request.getFirstName());
        driver.setLastName(request.getLastName());
        driver.setPhoneNumber(request.getPhoneNumber());
        driver.setMonthlyCost(request.getMonthlyCost());
        driver.setMonthlyWorkingHours(request.getMonthlyWorkingHours());

        Driver savedDriver = driverRepository.save(driver);
        return DriverUtils.toDriverResponse(savedDriver);
    }

    @Transactional
    public DriverResponse updateDriver(Long id, DriverRequest request) {
        Driver driver = driverRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Driver not found with id " + id));
        driver.setFirstName(request.getFirstName());
        driver.setLastName(request.getLastName());
        driver.setLogin(DriverUtils.generateUniqueLogin(driverRepository, request.getFirstName(), request.getLastName()));
        driver.setPhoneNumber(request.getPhoneNumber());
        driver.setMonthlyCost(request.getMonthlyCost());
        driver.setMonthlyWorkingHours(request.getMonthlyWorkingHours());

        Driver updatedDriver = driverRepository.save(driver);
        return DriverUtils.toDriverResponse(updatedDriver);
    }

    @Transactional
    public void deleteDriver(Long id) {
        if (!driverRepository.existsById(id)) {
            throw new EntityNotFoundException("Driver not found with id " + id);
        }

        driverRepository.deleteById(id);
    }
}