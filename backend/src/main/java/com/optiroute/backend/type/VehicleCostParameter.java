package com.optiroute.backend.type;

public enum VehicleCostParameter {

    INSURANCE("Assurance"), MAINTENANCE("Entretien");

    private final String label;

    VehicleCostParameter(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}