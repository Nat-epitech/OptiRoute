package com.optiroute.backend.client;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.optiroute.backend.config.HereApiProperties;

@Service
public class HereApiClient {

    private final RestClient restClient;
    private final HereApiProperties properties;

    public HereApiClient(RestClient.Builder builder, HereApiProperties properties) {
        this.restClient = builder.build();
        this.properties = properties;
    }

    public String getRoutes(String origin, String destination, String departureTime) {

        String url = UriComponentsBuilder
                .fromUriString("https://router.hereapi.com/v8/routes")
                .queryParam("transportMode", "truck")
                .queryParam("routingMode", "fast")
                .queryParam("alternatives", "3")
                .queryParam("origin", origin)
                .queryParam("destination", destination)
                .queryParam("departureTime", departureTime)
                .queryParam("return", "summary,travelSummary,tolls,polyline,actions,instructions")
                .queryParam("apikey", properties.getApiKey())
                .toUriString();

        return restClient.get().uri(url).retrieve().body(String.class);
    }
}