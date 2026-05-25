package com.optiroute.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.optiroute.backend.dto.response.FuelResponse;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.zip.GZIPInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class FuelPriceService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Double cachedDieselPrice;
    private Instant lastUpdate;

    public synchronized double getAverageDieselPrice() {

        if (cachedDieselPrice != null && lastUpdate != null
                && Instant.now().isBefore(lastUpdate.plus(Duration.ofHours(1)))) {
            return cachedDieselPrice;
        }

        String url = "https://data.economie.gouv.fr/api/explore/v2.1/catalog/datasets/prix-des-carburants-en-france-flux-instantane-v2/records?limit=100";
        try {
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            RestTemplate restTemplate = new RestTemplate(factory);
            ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, null, byte[].class);

            byte[] bodyBytes = response.getBody();
            if (bodyBytes == null) {
                return cachedDieselPrice != null ? cachedDieselPrice : 1.8;
            }

            String json;
            if (bodyBytes.length > 2 && (bodyBytes[0] == (byte) 0x1f) && (bodyBytes[1] == (byte) 0x8b)) {
                try (GZIPInputStream gis = new GZIPInputStream(new java.io.ByteArrayInputStream(bodyBytes));
                        ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                    gis.transferTo(out);
                    json = out.toString(java.nio.charset.StandardCharsets.UTF_8);
                }
            } else {
                json = new String(bodyBytes, java.nio.charset.StandardCharsets.UTF_8);
            }

            FuelResponse body = objectMapper.readValue(json, FuelResponse.class);
            double average = body.getResults().stream()
                    .map(FuelResponse.Result::getGazolePrix)
                    .filter(Objects::nonNull)
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(1.8);

            cachedDieselPrice = average;
            lastUpdate = Instant.now();

            return average;
        } catch (Exception e) {
            e.printStackTrace();
            return cachedDieselPrice != null ? cachedDieselPrice : 1.8;
        }
    }
}