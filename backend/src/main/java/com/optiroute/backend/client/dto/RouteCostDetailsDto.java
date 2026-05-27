package com.optiroute.backend.client.dto;

public class RouteCostDetailsDto {

    private double fuelCost;
    private double tollCost;
    private double driverCost;

    private double totalCost;

    public double getFuelCost() {
        return fuelCost;
    }

    public void setFuelCost(double fuelCost) {
        this.fuelCost = fuelCost;
    }

    public double getTollCost() {
        return tollCost;
    }

    public void setTollCost(double tollCost) {
        this.tollCost = tollCost;
    }

    public double getDriverCost() {
        return driverCost;
    }

    public void setDriverCost(double driverCost) {
        this.driverCost = driverCost;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
}