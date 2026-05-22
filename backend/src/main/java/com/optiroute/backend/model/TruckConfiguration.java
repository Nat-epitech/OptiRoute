package com.optiroute.backend.model;

public class TruckConfiguration {

    private double grossWeightKg;
    private double heightCm;
    private double widthCm;
    private double lengthCm;

    private double fuelConsumptionLitersPer100Km;

    public double getGrossWeightKg() {
        return grossWeightKg;
    }

    public void setGrossWeightKg(double grossWeightKg) {
        this.grossWeightKg = grossWeightKg;
    }

    public double getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(double heightCm) {
        this.heightCm = heightCm;
    }

    public double getWidthCm() {
        return widthCm;
    }

    public void setWidthCm(double widthCm) {
        this.widthCm = widthCm;
    }

    public double getLengthCm() {
        return lengthCm;
    }

    public void setLengthCm(double lengthCm) {
        this.lengthCm = lengthCm;
    }

    public double getFuelConsumptionLitersPer100Km() {
        return fuelConsumptionLitersPer100Km;
    }

    public void setFuelConsumptionLitersPer100Km(double fuelConsumptionLitersPer100Km) {
        this.fuelConsumptionLitersPer100Km = fuelConsumptionLitersPer100Km;
    }
}