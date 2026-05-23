package com.optiroute.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HereRouteParser {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<ParsedRoute> parseRoutes(String json) {
        try {
            List<ParsedRoute> parsedRoutes = new ArrayList<>();

            JsonNode root = objectMapper.readTree(json);
            JsonNode routes = root.path("routes");
            for (JsonNode route : routes) {

                JsonNode section = route.path("sections").get(0);

                long duration = section.path("summary").path("duration").asLong();

                long distance = section.path("summary").path("length").asLong();

                String polyline = section.path("polyline").asText();

                double tollCost = extractTollCost(section);

                parsedRoutes.add(new ParsedRoute(duration, distance, polyline, tollCost));
            }

            return parsedRoutes;
        } catch (Exception e) {
            throw new RuntimeException("HERE parsing failed", e);
        }
    }

    private double extractTollCost(JsonNode section) {
        JsonNode fares = section.path("tolls").path("fares");

        if (fares.isArray() && !fares.isEmpty()) {
            return fares.get(0).path("price").path("value").asDouble(0.0);
        }

        return 0.0;
    }

    public static class ParsedRoute {

        public long durationSeconds;
        public long distanceMeters;
        public String polyline;
        public double tollCost;

        public ParsedRoute(long durationSeconds, long distanceMeters, String polyline, double tollCost) {
            this.durationSeconds = durationSeconds;
            this.distanceMeters = distanceMeters;
            this.polyline = polyline;
            this.tollCost = tollCost;
        }
    }
}