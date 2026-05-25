package com.optiroute.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FuelResponse {

    private List<Result> results;

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {

        @JsonProperty("gazole_prix")
        private Double gazolePrix;

        public Double getGazolePrix() {
            return gazolePrix;
        }

        public void setGazolePrix(Double gazolePrix) {
            this.gazolePrix = gazolePrix;
        }
    }
}