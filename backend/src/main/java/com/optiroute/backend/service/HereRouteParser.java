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

                String rawJson = section.toString();

                parsedRoutes.add(new ParsedRoute(duration, distance, polyline, tollCost, rawJson));
            }

            return parsedRoutes;

        } catch (Exception e) {
            throw new RuntimeException("HERE parsing failed", e);
        }
    }

    private double extractTollCost(JsonNode section) {
        JsonNode tolls = section.path("tolls");
        if (!tolls.isArray()) {
            return 0.0;
        }

        double sum = 0.0;
        for (JsonNode tollSystem : tolls) {
            JsonNode fares = tollSystem.path("fares");

            if (fares.isArray()) {
                for (JsonNode fare : fares) {
                    sum += fare.path("price").path("value").asDouble(0.0);
                }
            }
        }

        return sum;
    }

    public static class ParsedRoute {

        public long durationSeconds;
        public long distanceMeters;
        public String polyline;
        public double tollCost;
        public String rawJson;

        public ParsedRoute(long durationSeconds, long distanceMeters, String polyline, double tollCost, String rawJson) {
            this.durationSeconds = durationSeconds;
            this.distanceMeters = distanceMeters;
            this.polyline = polyline;
            this.tollCost = tollCost;
            this.rawJson = rawJson;
        }
    }
}