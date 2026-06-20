package com.optiroute.backend.dto.response;

public class RouteDto {

    private Long id;

    private Double distanceKm;
    private Double durationMinutes;

    private String polyline;
    private String actions;

    private Double fuelCost;
    private Double tollCost;
    private Double driverCost;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public Double getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Double durationMinutes) {
        this.durationMinutes = durationMinutes;
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
}