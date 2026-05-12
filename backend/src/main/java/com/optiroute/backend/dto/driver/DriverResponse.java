package com.optiroute.backend.dto.driver;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DriverResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}