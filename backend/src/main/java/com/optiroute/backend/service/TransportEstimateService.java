package com.optiroute.backend.service;

import com.optiroute.backend.repository.TransportEstimateRepository;
import com.optiroute.backend.entity.TransportEstimate;
import com.optiroute.backend.entity.Transport;
import com.optiroute.backend.dto.dto.RoutesDto;
import com.optiroute.backend.dto.response.RouteResponse;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransportEstimateService {

	private final TransportEstimateRepository transportEstimateRepository;

	public TransportEstimateService(TransportEstimateRepository transportEstimateRepository) {
		this.transportEstimateRepository = transportEstimateRepository;
	}

	@Transactional
	public TransportEstimate saveEstimate(Transport transport, RouteResponse response) {
		// TODO: Choisir route adapter selon critère (coût, distance, durée, etc.)
		RoutesDto bestRoute = response.getRoutes().getFirst();

		TransportEstimate estimate = new TransportEstimate();
		estimate.setTransportId(transport.getId());
		estimate.setRoutingProvider("HERE");
		estimate.setRoutingMode("FASTEST");
		estimate.setDepartureTime(transport.getPlannedStart());
		estimate.setDistanceMeters(bestRoute.getDistanceMeters());
		estimate.setDurationSeconds(bestRoute.getDurationSeconds());

		// TODO : lorsque HERE exposera la durée avec trafic
		estimate.setTrafficDurationSeconds(null);
		estimate.setPolyline(bestRoute.getPolyline());

		estimate.setEstimatedFuelCost(BigDecimal.valueOf(bestRoute.getCosts().getFuelCost()));
		estimate.setEstimatedTollCost(BigDecimal.valueOf(bestRoute.getCosts().getTollCost()));
		estimate.setEstimatedDriverCost(BigDecimal.valueOf(bestRoute.getCosts().getDriverCost()));
		estimate.setEstimatedTotalCost(BigDecimal.valueOf(bestRoute.getCosts().getTotalCost()));

		return transportEstimateRepository.save(estimate);
	}

	@Transactional
	public TransportEstimate saveEstimate(Transport transport, RoutesDto route, String routingProvider, String routingMode) {

		TransportEstimate estimate = new TransportEstimate();

		estimate.setTransportId(transport.getId());
		estimate.setRoutingProvider(routingProvider);
		estimate.setRoutingMode(routingMode);
		estimate.setDepartureTime(transport.getPlannedStart());

		estimate.setDistanceMeters(route.getDistanceMeters());
		estimate.setDurationSeconds(route.getDurationSeconds());
		estimate.setTrafficDurationSeconds(null);
		estimate.setPolyline(route.getPolyline());

		estimate.setEstimatedFuelCost(BigDecimal.valueOf(route.getCosts().getFuelCost()));
		estimate.setEstimatedTollCost(BigDecimal.valueOf(route.getCosts().getTollCost()));
		estimate.setEstimatedDriverCost(BigDecimal.valueOf(route.getCosts().getDriverCost()));
		estimate.setEstimatedTotalCost(BigDecimal.valueOf(route.getCosts().getTotalCost()));

		return transportEstimateRepository.save(estimate);
	}

}