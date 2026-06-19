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

                List<RouteAction> actions = extractActions(section);

                String rawJson = section.toString();

                parsedRoutes.add(new ParsedRoute(duration, distance, polyline, tollCost, actions, rawJson));
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

    private List<RouteAction> extractActions(JsonNode section) {
        List<RouteAction> actions = new ArrayList<>();

        JsonNode actionsNode = section.path("actions");

        if (!actionsNode.isArray()) {
            return actions;
        }

        for (JsonNode action : actionsNode) {

            RouteAction a = new RouteAction();
            a.setAction(action.path("action").asText());
            a.setInstruction(action.path("instruction").asText());
            a.setOffset(action.path("offset").asLong());
            a.setLength(action.path("length").asLong());
            a.setDuration(action.path("duration").asLong());

            actions.add(a);
        }

        return actions;
    }

    public static class RouteAction {
        private String action;
        private String instruction;
        private long offset;
        private long length;
        private long duration;

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getInstruction() {
            return instruction;
        }

        public void setInstruction(String instruction) {
            this.instruction = instruction;
        }

        public long getOffset() {
            return offset;
        }

        public void setOffset(long offset) {
            this.offset = offset;
        }

        public long getLength() {
            return length;
        }

        public void setLength(long length) {
            this.length = length;
        }

        public long getDuration() {
            return duration;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }
    }

    public static class ParsedRoute {

        public long durationSeconds;
        public long distanceMeters;
        public String polyline;
        public double tollCost;

        public List<RouteAction> actions;
        public String rawJson;

        public ParsedRoute(long durationSeconds, long distanceMeters, String polyline, double tollCost, List<RouteAction> actions,
                String rawJson) {
            this.durationSeconds = durationSeconds;
            this.distanceMeters = distanceMeters;
            this.polyline = polyline;
            this.tollCost = tollCost;
            this.actions = actions;
            this.rawJson = rawJson;
        }
    }
}