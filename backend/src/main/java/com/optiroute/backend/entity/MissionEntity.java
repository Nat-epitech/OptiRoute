package com.optiroute.backend.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "mission")
public class MissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private RouteEntity route;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private DriverEntity driver;

    @Column(name = "planned_start", nullable = false)
    private OffsetDateTime plannedStart;

    @Column(name = "planned_end")
    private OffsetDateTime plannedEnd;

    @Column(nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    // Persist callback to set default values before saving

    @PrePersist
    public void prePersist() {
        this.createdAt = OffsetDateTime.now();

        if (this.status == null) {
            this.status = "PLANNED";
        }
    }

    // Getters and Setters

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public RouteEntity getRoute() {
        return route;
    }

    public void setRoute(RouteEntity route) {
        this.route = route;
    }

    public DriverEntity getDriver() {
        return driver;
    }

    public void setDriver(DriverEntity driver) {
        this.driver = driver;
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

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}