package com.optiroute.backend.service.transport;

import com.optiroute.backend.dto.response.route.RouteResponse;
import com.optiroute.backend.dto.response.route.RoutesDto;
import com.optiroute.backend.entity.transport.Transport;
import com.optiroute.backend.entity.transport.TransportEstimate;
import com.optiroute.backend.repository.transport.TransportEstimateRepository;

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
		estimate.setDepartureTime(transport.getPlannedStart());
		estimate.setDistanceMeters(bestRoute.getDistanceMeters());
		estimate.setDurationSeconds(bestRoute.getDuration());
		estimate.setPolyline(bestRoute.getPolyline());

		estimate.setEstimatedFuelCost(BigDecimal.valueOf(bestRoute.getCosts().getFuelCost()));
		estimate.setEstimatedTollCost(BigDecimal.valueOf(bestRoute.getCosts().getTollCost()));

		return transportEstimateRepository.save(estimate);
	}

	@Transactional
	public TransportEstimate saveEstimate(Transport transport, RoutesDto route) {

		TransportEstimate estimate = new TransportEstimate();

		estimate.setTransportId(transport.getId());
		estimate.setDepartureTime(transport.getPlannedStart());

		estimate.setDistanceMeters(route.getDistanceMeters());
		estimate.setDurationSeconds(route.getDuration());
		estimate.setPolyline(route.getPolyline());

		estimate.setEstimatedFuelCost(BigDecimal.valueOf(route.getCosts().getFuelCost()));
		estimate.setEstimatedTollCost(BigDecimal.valueOf(route.getCosts().getTollCost()));

		return transportEstimateRepository.save(estimate);
	}

}