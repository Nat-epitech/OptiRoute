package com.optiroute.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "transport_estimate")
public class TransportEstimate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transport_id", nullable = false, unique = true)
    private Long transportId;

    @Column(name = "routing_provider", nullable = false, length = 50)
    private String routingProvider;

    @Column(name = "routing_mode", nullable = false, length = 20)
    private String routingMode;

    @Column(name = "departure_time")
    private OffsetDateTime departureTime;

    @Column(name = "distance_meters", nullable = false)
    private Long distanceMeters;

    @Column(name = "duration_seconds", nullable = false)
    private Long durationSeconds;

    @Column(name = "traffic_duration_seconds")
    private Long trafficDurationSeconds;

    @Column(name = "polyline", nullable = false, columnDefinition = "TEXT")
    private String polyline;

    @Column(name = "estimated_fuel_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal estimatedFuelCost;

    @Column(name = "estimated_toll_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal estimatedTollCost;

    @Column(name = "estimated_driver_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal estimatedDriverCost;

    @Column(name = "estimated_total_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal estimatedTotalCost;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = OffsetDateTime.now();
    }

    // ===== getters / setters =====

    public Long getId() {
        return id;
    }

    public Long getTransportId() {
        return transportId;
    }

    public void setTransportId(Long transportId) {
        this.transportId = transportId;
    }

    public String getRoutingProvider() {
        return routingProvider;
    }

    public void setRoutingProvider(String routingProvider) {
        this.routingProvider = routingProvider;
    }

    public String getRoutingMode() {
        return routingMode;
    }

    public void setRoutingMode(String routingMode) {
        this.routingMode = routingMode;
    }

    public OffsetDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(OffsetDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public Long getDistanceMeters() {
        return distanceMeters;
    }

    public void setDistanceMeters(Long distanceMeters) {
        this.distanceMeters = distanceMeters;
    }

    public Long getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Long durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public Long getTrafficDurationSeconds() {
        return trafficDurationSeconds;
    }

    public void setTrafficDurationSeconds(Long trafficDurationSeconds) {
        this.trafficDurationSeconds = trafficDurationSeconds;
    }

    public String getPolyline() {
        return polyline;
    }

    public void setPolyline(String polyline) {
        this.polyline = polyline;
    }

    public BigDecimal getEstimatedFuelCost() {
        return estimatedFuelCost;
    }

    public void setEstimatedFuelCost(BigDecimal estimatedFuelCost) {
        this.estimatedFuelCost = estimatedFuelCost;
    }

    public BigDecimal getEstimatedTollCost() {
        return estimatedTollCost;
    }

    public void setEstimatedTollCost(BigDecimal estimatedTollCost) {
        this.estimatedTollCost = estimatedTollCost;
    }

    public BigDecimal getEstimatedDriverCost() {
        return estimatedDriverCost;
    }

    public void setEstimatedDriverCost(BigDecimal estimatedDriverCost) {
        this.estimatedDriverCost = estimatedDriverCost;
    }

    public BigDecimal getEstimatedTotalCost() {
        return estimatedTotalCost;
    }

    public void setEstimatedTotalCost(BigDecimal estimatedTotalCost) {
        this.estimatedTotalCost = estimatedTotalCost;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}