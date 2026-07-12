package com.optiroute.backend.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;

@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "external_source")
    private String externalSource = "MANUAL";

    @Column(nullable = false, unique = true)
    private String registration;

    private String brand;

    private String model;

    @Column(name = "fuel_type")
    private String fuelType;

    @Column(name = "average_consumption", precision = 5, scale = 2)
    private BigDecimal averageConsumption;

    @Column(name = "tank_capacity", precision = 5, scale = 2)
    private BigDecimal tankCapacity;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private JsonNode metadata;

    @PrePersist
    void prePersist() {
        createdAt = OffsetDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = OffsetDateTime.now();
    }

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

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public BigDecimal getAverageConsumption() {
        return averageConsumption;
    }

    public void setAverageConsumption(BigDecimal averageConsumption) {
        this.averageConsumption = averageConsumption;
    }

    public BigDecimal getTankCapacity() {
        return tankCapacity;
    }

    public void setTankCapacity(BigDecimal tankCapacity) {
        this.tankCapacity = tankCapacity;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public JsonNode getMetadata() {
        return metadata;
    }

    public void setMetadata(JsonNode metadata) {
        this.metadata = metadata;
    }

}