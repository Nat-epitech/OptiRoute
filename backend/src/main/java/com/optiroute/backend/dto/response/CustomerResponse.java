package com.optiroute.backend.dto.response;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.databind.JsonNode;

public record CustomerResponse(
        Long id,
        String externalId,
        String externalSource,
        String name,
        String code,
        String address,
        String city,
        String country,
        JsonNode metadata,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) {
}