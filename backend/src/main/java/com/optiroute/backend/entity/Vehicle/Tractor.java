package com.optiroute.backend.entity.vehicle;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.optiroute.backend.entity.EntityUtils;

@Entity
@Table(name = "tractor")
@NoArgsConstructor
public class Tractor extends EntityUtils {

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

    private Integer maxSpeed;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private FuelType fuelType;

    @Column(precision = 6, scale = 2)
    private BigDecimal averageConsumption;

    private Integer emptyWeightKg;

    private Integer grossCombinationWeightKg;

    private Integer heightCm;

    private Integer widthCm;

    private Integer lengthCm;

    private Integer axleCount;

    @Column(precision = 12, scale = 2)
    private BigDecimal purchaseCost;

    private LocalDate depreciationStartDate;

    private LocalDate depreciationEndDate;

    @Column(name = "is_active", nullable = false)
    private Boolean active = true;

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

    public Integer getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Integer maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public BigDecimal getAverageConsumption() {
        return averageConsumption;
    }

    public void setAverageConsumption(BigDecimal averageConsumption) {
        this.averageConsumption = averageConsumption;
    }

    public Integer getEmptyWeightKg() {
        return emptyWeightKg;
    }

    public void setEmptyWeightKg(Integer emptyWeightKg) {
        this.emptyWeightKg = emptyWeightKg;
    }

    public Integer getGrossCombinationWeightKg() {
        return grossCombinationWeightKg;
    }

    public void setGrossCombinationWeightKg(Integer grossCombinationWeightKg) {
        this.grossCombinationWeightKg = grossCombinationWeightKg;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

}