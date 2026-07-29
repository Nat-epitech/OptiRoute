package com.optiroute.backend.utils;

import com.optiroute.backend.dto.response.vehicle.SemiTrailerLightResponse;
import com.optiroute.backend.dto.response.vehicle.SemiTrailerResponse;
import com.optiroute.backend.entity.vehicle.SemiTrailer;

public class VehicleUtils {
    public static SemiTrailerResponse toSemiTrailerResponse(SemiTrailer semiTrailer) {
        return new SemiTrailerResponse(
                semiTrailer.getId(),
                semiTrailer.getExternalId(),
                semiTrailer.getExternalSource(),
                semiTrailer.getRegistration(),
                semiTrailer.getBrand(),
                semiTrailer.getModel(),
                semiTrailer.getTrailerType(),
                semiTrailer.getMaxSpeed(),
                semiTrailer.getEmptyWeightKg(),
                semiTrailer.getGrossVehicleWeightKg(),
                semiTrailer.getHeightCm(),
                semiTrailer.getWidthCm(),
                semiTrailer.getLengthCm(),
                semiTrailer.getAxleCount(),
                semiTrailer.getPurchaseCost(),
                semiTrailer.getDepreciationStartDate(),
                semiTrailer.getDepreciationEndDate(),
                semiTrailer.getActive(),
                semiTrailer.getCreatedAt(),
                semiTrailer.getUpdatedAt());
    }

    public static SemiTrailerLightResponse toSemiTrailerLightResponse(SemiTrailer semiTrailer) {
        return new SemiTrailerLightResponse(
                semiTrailer.getId(),
                semiTrailer.getRegistration(),
                semiTrailer.getBrand(),
                semiTrailer.getModel());
    }
}
