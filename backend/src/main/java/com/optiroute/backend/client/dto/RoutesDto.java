package com.optiroute.backend.client.dto;

import java.util.List;

public class RoutesDto {

    private long durationSeconds;
    private long distanceMeters;

    private double originLat;
    private double originLng;

    private double destinationLat;
    private double destinationLng;

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

    public double getOriginLat() {
        return originLat;
    }

    public void setOriginLat(double originLat) {
        this.originLat = originLat;
    }

    public double getOriginLng() {
        return originLng;
    }

    public void setOriginLng(double originLng) {
        this.originLng = originLng;
    }

    public double getDestinationLat() {
        return destinationLat;
    }

    public void setDestinationLat(double destinationLat) {
        this.destinationLat = destinationLat;
    }

    public double getDestinationLng() {
        return destinationLng;
    }

    public void setDestinationLng(double destinationLng) {
        this.destinationLng = destinationLng;
    }
}