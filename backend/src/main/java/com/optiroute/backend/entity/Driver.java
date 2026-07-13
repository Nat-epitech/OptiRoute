package com.optiroute.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "driver")
@Getter
@Setter
public class Driver extends EntityUtils {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phoneNumber;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(name = "hourly_rate", precision = 8, scale = 2)
    private BigDecimal hourlyRate;
}