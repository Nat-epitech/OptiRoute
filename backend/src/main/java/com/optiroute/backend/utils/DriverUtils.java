package com.optiroute.backend.utils;

import com.optiroute.backend.dto.response.driver.DriverResponse;
import com.optiroute.backend.dto.response.driver.DriverSummaryResponse;
import com.optiroute.backend.repository.driver.DriverRepository;
import com.optiroute.backend.entity.driver.Driver;

public class DriverUtils {
    public static DriverResponse toDriverResponse(Driver driver) {
        return new DriverResponse(
                driver.getId(),
                driver.getLogin(),
                driver.getFirstName(),
                driver.getLastName(),
                driver.getPhoneNumber(),
                driver.getMonthlyCost(),
                driver.getMonthlyWorkingHours());
    }

    public static DriverSummaryResponse toSummaryResponse(Driver driver) {
        return new DriverSummaryResponse(
                driver.getId(),
                driver.getFirstName(),
                driver.getLastName());
    }

    public static String generateUniqueLogin(DriverRepository driverRepository, String firstName, String lastName) {
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
