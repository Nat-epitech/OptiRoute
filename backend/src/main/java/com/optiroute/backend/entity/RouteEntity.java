package com.optiroute.backend.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "route")
public class RouteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double originLat;
    private Double originLng;

    private Double destinationLat;
    private Double destinationLng;

    private Double durationMinutes;
    private Double distanceKm;

    private Double fuelCost;
    private Double tollCost;
    private Double driverCost;

    private OffsetDateTime departureTime;
    private OffsetDateTime arrivalTime;

    @Lob
    @Column(columnDefinition = "text")
    private String polyline;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String actions;

    @Lob
    @Column(columnDefinition = "text")
    private String hereData;

    private OffsetDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getOriginLat() {
        return originLat;
    }

    public void setOriginLat(Double originLat) {
        this.originLat = originLat;
    }

    public Double getOriginLng() {
        return originLng;
    }

    public void setOriginLng(Double originLng) {
        this.originLng = originLng;
    }

    public Double getDestinationLat() {
        return destinationLat;
    }

    public void setDestinationLat(Double destinationLat) {
        this.destinationLat = destinationLat;
    }

    public Double getDestinationLng() {
        return destinationLng;
    }

    public void setDestinationLng(Double destinationLng) {
        this.destinationLng = destinationLng;
    }

    public Double getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Double durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public Double getFuelCost() {
        return fuelCost;
    }

    public void setFuelCost(Double fuelCost) {
        this.fuelCost = fuelCost;
    }

    public Double getTollCost() {
        return tollCost;
    }

    public void setTollCost(Double tollCost) {
        this.tollCost = tollCost;
    }

    public Double getDriverCost() {
        return driverCost;
    }

    public void setDriverCost(Double driverCost) {
        this.driverCost = driverCost;
    }

    public OffsetDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(OffsetDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public OffsetDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(OffsetDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getPolyline() {
        return polyline;
    }

    public void setPolyline(String polyline) {
        this.polyline = polyline;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public String getHereData() {
        return hereData;
    }

    public void setHereData(String hereData) {
        this.hereData = hereData;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}