package com.optiroute.backend.service.driver;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.optiroute.backend.dto.request.driver.DriverRequest;
import com.optiroute.backend.dto.response.driver.DriverResponse;
import com.optiroute.backend.entity.driver.Driver;
import com.optiroute.backend.repository.driver.DriverRepository;

import com.optiroute.backend.utils.CommonUtils;

import java.util.List;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository, PasswordEncoder passwordEncoder) {
        this.driverRepository = driverRepository;
    }

    public List<DriverResponse> getAll() {
        return driverRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public DriverResponse getById(Long id) {
        Driver driver = driverRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Driver not found with id: " + id));

        return toResponse(driver);
    }

    @Transactional
    public DriverResponse createDriver(DriverRequest request) {
        Driver driver = new Driver();
        driver.setLogin(generateUniqueLogin(request.getFirstName(), request.getLastName()));
        driver.setFirstName(request.getFirstName());
        driver.setLastName(request.getLastName());
        driver.setPhoneNumber(request.getPhoneNumber());
        driver.setMonthlyCost(request.getMonthlyCost());
        driver.setMonthlyWorkingHours(request.getMonthlyWorkingHours());

        Driver savedDriver = driverRepository.save(driver);
        return toResponse(savedDriver);
    }

    @Transactional
    public DriverResponse updateDriver(Long id, DriverRequest request) {
        Driver driver = driverRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Driver not found with id " + id));
        driver.setFirstName(request.getFirstName());
        driver.setLastName(request.getLastName());
        driver.setLogin(generateUniqueLogin(request.getFirstName(), request.getLastName()));
        driver.setPhoneNumber(request.getPhoneNumber());
        driver.setMonthlyCost(request.getMonthlyCost());
        driver.setMonthlyWorkingHours(request.getMonthlyWorkingHours());

        Driver updatedDriver = driverRepository.save(driver);
        return toResponse(updatedDriver);
    }

    @Transactional
    public void deleteDriver(Long id) {
        if (!driverRepository.existsById(id)) {
            throw new EntityNotFoundException("Driver not found with id " + id);
        }

        driverRepository.deleteById(id);
    }

    private DriverResponse toResponse(Driver driver) {
        return DriverResponse.builder()
                .id(driver.getId())
                .login(driver.getLogin())
                .firstName(driver.getFirstName())
                .lastName(driver.getLastName())
                .phoneNumber(driver.getPhoneNumber())
                .monthlyCost(driver.getMonthlyCost())
                .monthlyWorkingHours(driver.getMonthlyWorkingHours())
                .build();
    }

    private String generateUniqueLogin(String firstName, String lastName) {
        String normalizedFirstName = CommonUtils.normalizeText(firstName).toUpperCase();
        String normalizedLastName = CommonUtils.normalizeText(lastName).toUpperCase();

        String baseLogin = normalizedFirstName.substring(0, 1) + normalizedLastName;
        String login = baseLogin;
        int suffix = 2;

        while (driverRepository.existsByLogin(login)) {
            login = baseLogin + suffix;
            suffix++;
        }

        return login;
    }
}