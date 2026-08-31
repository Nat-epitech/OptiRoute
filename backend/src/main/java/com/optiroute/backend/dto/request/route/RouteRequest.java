package com.optiroute.backend.dto.request.route;

import com.optiroute.backend.model.Position;
import com.optiroute.backend.type.GpsModeType;

import java.time.OffsetDateTime;

public class RouteRequest {

    private Position origin;
    private Position destination;

    private GpsModeType mode;

    private OffsetDateTime departureTime;

    private Integer maxTravelTimeMinutes;

    private Long tractorId;
    private Long semiTrailerId;

    private boolean emptyTrip;

    public Position getOrigin() {
        return origin;
    }

    public void setOrigin(Position origin) {
        this.origin = origin;
    }

    public Position getDestination() {
        return destination;
    }

    public void setDestination(Position destination) {
        this.destination = destination;
    }

    public GpsModeType getMode() {
        return mode;
    }

    public void setMode(GpsModeType mode) {
        this.mode = mode;
    }

    public OffsetDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(OffsetDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public Integer getMaxTravelTimeMinutes() {
        return maxTravelTimeMinutes;
    }

    public void setMaxTravelTimeMinutes(Integer maxTravelTimeMinutes) {
        this.maxTravelTimeMinutes = maxTravelTimeMinutes;
    }

    public Long getTractorId() {
        return tractorId;
    }

    public void setTractorId(Long tractorId) {
        this.tractorId = tractorId;
    }

    public Long getSemiTrailerId() {
        return semiTrailerId;
    }

    public void setSemiTrailerId(Long semiTrailerId) {
        this.semiTrailerId = semiTrailerId;
    }

    public boolean isEmptyTrip() {
        return emptyTrip;
    }

    public void setEmptyTrip(boolean emptyTrip) {
        this.emptyTrip = emptyTrip;
    }

}