package com.optiroute.backend.dto.response;

import java.time.OffsetDateTime;

public class MissionResponse {

    private Long id;

    private Long driverId;

    private String name;

    private OffsetDateTime plannedStart;
    private OffsetDateTime plannedEnd;

    private String status;

    private RouteDto route;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OffsetDateTime getPlannedStart() {
        return plannedStart;
    }

    public void setPlannedStart(OffsetDateTime plannedStart) {
        this.plannedStart = plannedStart;
    }

    public OffsetDateTime getPlannedEnd() {
        return plannedEnd;
    }

    public void setPlannedEnd(OffsetDateTime plannedEnd) {
        this.plannedEnd = plannedEnd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RouteDto getRoute() {
        return route;
    }

    public void setRoute(RouteDto route) {
        this.route = route;
    }
}