package com.optiroute.backend.dto.response;

import java.time.LocalDateTime;

public record MissionResponse(
        Long id,
        String title,
        LocalDateTime start,
        LocalDateTime end) {
}