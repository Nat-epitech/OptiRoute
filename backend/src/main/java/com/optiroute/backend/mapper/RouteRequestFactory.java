package com.optiroute.backend.mapper;

import com.optiroute.backend.dto.request.RouteRequest;
import com.optiroute.backend.entity.Driver;
import com.optiroute.backend.entity.Transport;
import com.optiroute.backend.model.RouteMode;
import com.optiroute.backend.model.TruckConfiguration;
import com.optiroute.backend.model.Position;
import com.optiroute.backend.entity.Vehicle;

import org.springframework.stereotype.Component;

@Component
public class RouteRequestFactory {

	public RouteRequest fromTransport(Transport transport, Vehicle vehicle, Driver driver) {

		RouteRequest request = new RouteRequest();

		// Départ
		Position origin = new Position();
		origin.setLat(transport.getOriginLat());
		origin.setLng(transport.getOriginLng());
		request.setOrigin(origin);

		// Destination
		Position destination = new Position();
		destination.setLat(transport.getDestinationLat());
		destination.setLng(transport.getDestinationLng());
		request.setDestination(destination);

		// Heure de départ
		request.setDepartureTime(transport.getPlannedStart());

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