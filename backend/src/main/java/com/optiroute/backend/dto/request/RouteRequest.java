package com.optiroute.backend.dto.request;

import com.optiroute.backend.model.Position;
import com.optiroute.backend.model.RouteMode;
import com.optiroute.backend.model.TruckConfiguration;

import java.time.OffsetDateTime;

public class RouteRequest {

    private Position origin;
    private Position destination;

    private RouteMode mode;

    private OffsetDateTime departureTime;

    private Integer maxTravelTimeMinutes;

    private Double fuelPricePerLiter;

    private Double driverHourlyRate;

    private TruckConfiguration truck;

    public Position getOrigin() {
        return origin;
    }

    public void setOrigin(Position origin) {
        this.origin = origin;
    }

    public Position getDestination() {
        return destination;
    }

    public void setDestination(Position destination) {
        this.destination = destination;
    }

    public RouteMode getMode() {
        return mode;
    }

    public void setMode(RouteMode mode) {
        this.mode = mode;
    }

    public OffsetDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(OffsetDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public Integer getMaxTravelTimeMinutes() {
        return maxTravelTimeMinutes;
    }

    public void setMaxTravelTimeMinutes(Integer maxTravelTimeMinutes) {
        this.maxTravelTimeMinutes = maxTravelTimeMinutes;
    }

    public Double getFuelPricePerLiter() {
        return fuelPricePerLiter;
    }

    public void setFuelPricePerLiter(Double fuelPricePerLiter) {
        this.fuelPricePerLiter = fuelPricePerLiter;
    }

    public Double getDriverHourlyRate() {
        return driverHourlyRate;
    }

    public void setDriverHourlyRate(Double driverHourlyRate) {
        this.driverHourlyRate = driverHourlyRate;
    }

    public TruckConfiguration getTruck() {
        return truck;
    }

    public void setTruck(TruckConfiguration truck) {
        this.truck = truck;
    }
}