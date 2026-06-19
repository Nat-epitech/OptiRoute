package com.optiroute.backend.client.dto;

import java.util.List;

public class RoutesDto {

    private long durationSeconds;
    private long distanceMeters;

    private String polyline;
    private List<RouteActionDto> actions;
    private RouteCostDetailsDto costs;
    private String rawHereData;

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

    public List<RouteActionDto> getActions() {
        return actions;
    }

    public void setActions(List<RouteActionDto> actions) {
        this.actions = actions;
    }

    public String getRawHereData() {
        return rawHereData;
    }

    public void setRawHereData(String rawHereData) {
        this.rawHereData = rawHereData;
    }
}