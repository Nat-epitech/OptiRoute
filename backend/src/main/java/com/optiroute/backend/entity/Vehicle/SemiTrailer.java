package com.optiroute.backend.entity.vehicle;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.optiroute.backend.entity.EntityUtils;
import com.optiroute.backend.type.TrailerType;

@Entity
@Table(name = "semi_trailer")
@NoArgsConstructor
public class SemiTrailer extends EntityUtils {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "external_source")
    private String externalSource = "MANUAL";

    @Column(nullable = false, unique = true, length = 15)
    private String registration;

    @Column(length = 100)
    private String brand;

    @Column(length = 100)
    private String model;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private TrailerType trailerType;

    private Integer maxSpeed;

    private Integer emptyWeightKg;

    private Integer grossVehicleWeightKg;

    private Integer heightCm;

    private Integer widthCm;

    private Integer lengthCm;

    private Integer axleCount;

    @Column(precision = 12, scale = 2)
    private BigDecimal purchaseCost;

    private LocalDate depreciationStartDate;

    private LocalDate depreciationEndDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getExternalSource() {
        return externalSource;
    }

    public void setExternalSource(String externalSource) {
        this.externalSource = externalSource;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public TrailerType getTrailerType() {
        return trailerType;
    }

    public void setTrailerType(TrailerType trailerType) {
        this.trailerType = trailerType;
    }

    public Integer getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Integer maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Integer getEmptyWeightKg() {
        return emptyWeightKg;
    }

    public void setEmptyWeightKg(Integer emptyWeightKg) {
        this.emptyWeightKg = emptyWeightKg;
    }

    public Integer getGrossVehicleWeightKg() {
        return grossVehicleWeightKg;
    }

    public void setGrossVehicleWeightKg(Integer grossVehicleWeightKg) {
        this.grossVehicleWeightKg = grossVehicleWeightKg;
    }

    public Integer getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(Integer heightCm) {
        this.heightCm = heightCm;
    }

    public Integer getWidthCm() {
        return widthCm;
    }

    public void setWidthCm(Integer widthCm) {
        this.widthCm = widthCm;
    }

    public Integer getLengthCm() {
        return lengthCm;
    }

    public void setLengthCm(Integer lengthCm) {
        this.lengthCm = lengthCm;
    }

    public Integer getAxleCount() {
        return axleCount;
    }

    public void setAxleCount(Integer axleCount) {
        this.axleCount = axleCount;
    }

    public BigDecimal getPurchaseCost() {
        return purchaseCost;
    }

    public void setPurchaseCost(BigDecimal purchaseCost) {
        this.purchaseCost = purchaseCost;
    }

    public LocalDate getDepreciationStartDate() {
        return depreciationStartDate;
    }

    public void setDepreciationStartDate(LocalDate depreciationStartDate) {
        this.depreciationStartDate = depreciationStartDate;
    }

    public LocalDate getDepreciationEndDate() {
        return depreciationEndDate;
    }

    public void setDepreciationEndDate(LocalDate depreciationEndDate) {
        this.depreciationEndDate = depreciationEndDate;
    }

}