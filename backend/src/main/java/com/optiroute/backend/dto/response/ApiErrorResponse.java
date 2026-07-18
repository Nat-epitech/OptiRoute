package com.optiroute.backend.dto.response;

import java.time.Instant;

public record ApiErrorResponse(
        int status,
        String code,
        String message,
        String path,
        Instant timestamp) {
}
