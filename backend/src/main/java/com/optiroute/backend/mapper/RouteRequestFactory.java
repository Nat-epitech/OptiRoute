package com.optiroute.backend.mapper;

import com.optiroute.backend.dto.request.RouteRequest;
import com.optiroute.backend.entity.Driver;
import com.optiroute.backend.entity.Mission;
import com.optiroute.backend.model.RouteMode;
import com.optiroute.backend.model.TruckConfiguration;
import com.optiroute.backend.model.Position;
import com.optiroute.backend.entity.Vehicle;

import org.springframework.stereotype.Component;

@Component
public class RouteRequestFactory {

	public RouteRequest fromMission(Mission mission, Vehicle vehicle, Driver driver) {

		RouteRequest request = new RouteRequest();

		// Départ
		Position origin = new Position();
		origin.setLat(mission.getOriginLat());
		origin.setLng(mission.getOriginLng());
		request.setOrigin(origin);

		// Destination
		Position destination = new Position();
		destination.setLat(mission.getDestinationLat());
		destination.setLng(mission.getDestinationLng());
		request.setDestination(destination);

		// Heure de départ
		request.setDepartureTime(mission.getPlannedStart());

		// Mode de calcul (à adapter selon ton enum)
		request.setMode(RouteMode.FASTEST);

		// Coût horaire chauffeur
		/*
		 * if (driver.getHourlyRate() != null) {
		 * request.setDriverHourlyRate(driver.getHourlyRate().doubleValue());
		 * }
		 */

		// Configuration camion
		TruckConfiguration truck = new TruckConfiguration();

		if (vehicle.getAverageConsumption() != null) {
			truck.setFuelConsumptionLitersPer100Km(vehicle.getAverageConsumption().doubleValue());
		}

		request.setTruck(truck);

		return request;
	}
}