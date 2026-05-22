package com.optiroute.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class HereRouteParser {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ParsedRoute parse(String json) {

        try {
            JsonNode root = objectMapper.readTree(json);

            JsonNode route = root
                    .path("routes")
                    .get(0);

            JsonNode section = route
                    .path("sections")
                    .get(0);

            long duration = section
                    .path("summary")
                    .path("duration")
                    .asLong();

            long distance = section
                    .path("summary")
                    .path("length")
                    .asLong();

            String polyline = section
                    .path("polyline")
                    .asText();

            return new ParsedRoute(duration, distance, polyline);

        } catch (Exception e) {
            throw new RuntimeException("HERE parsing failed", e);
        }
    }

    public static class ParsedRoute {

        public long durationSeconds;
        public long distanceMeters;
        public String polyline;

        public ParsedRoute(long durationSeconds, long distanceMeters, String polyline) {
            this.durationSeconds = durationSeconds;
            this.distanceMeters = distanceMeters;
            this.polyline = polyline;
        }
    }
}