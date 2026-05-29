package com.optiroute.backend.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record PlanningEventRequest(

        @NotNull Long driverId,

        @NotNull String title,

        @NotNull LocalDateTime startDatetime,

        @NotNull LocalDateTime endDatetime

) {
}