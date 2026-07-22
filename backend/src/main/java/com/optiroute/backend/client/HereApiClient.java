package com.optiroute.backend.client;

import java.net.URI;

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
        URI uri = UriComponentsBuilder
                .fromUriString("https://router.hereapi.com/v8/routes")
                .queryParam("origin", origin)
                .queryParam("destination", destination)
                .queryParam("alternatives", 2)
                .queryParam("return", "polyline,summary")
                .queryParam("transportMode", "truck")
                .queryParam("routingMode", "fast")
                .queryParam("vehicle[grossWeight]", 44000)
                .queryParam("departureTime", departureTime)
                .queryParam("apikey", properties.getApiKey())
                .build().encode().toUri();

        return restClient.get().uri(uri).retrieve().body(String.class);
    }
}