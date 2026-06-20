package com.optiroute.backend.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

import com.optiroute.backend.client.dto.RoutesDto;

public record MissionRequest(

        @NotNull Long driverId,

        @NotNull String name,

        @NotNull OffsetDateTime plannedStart,

        @NotNull OffsetDateTime plannedEnd,

        @NotNull RoutesDto selectedRoute) {
}