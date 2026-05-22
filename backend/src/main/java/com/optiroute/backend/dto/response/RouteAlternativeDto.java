package com.optiroute.backend.dto.response;

public class RouteAlternativeDto {

    private long durationSeconds;
    private long distanceMeters;

    private String polyline;

    private RouteCostDetailsDto costs;

    public long getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(long durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public long getDistanceMeters() {
        return distanceMeters;
    }

    public void setDistanceMeters(long distanceMeters) {
        this.distanceMeters = distanceMeters;
    }

    public String getPolyline() {
        return polyline;
    }

    public void setPolyline(String polyline) {
        this.polyline = polyline;
    }

    public RouteCostDetailsDto getCosts() {
        return costs;
    }

    public void setCosts(RouteCostDetailsDto costs) {
        this.costs = costs;
    }
}