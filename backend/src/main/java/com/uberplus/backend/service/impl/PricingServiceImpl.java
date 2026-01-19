package com.uberplus.backend.service.impl;

import com.uberplus.backend.dto.ride.RideEstimateDTO;
import com.uberplus.backend.service.PricingService;
import org.springframework.stereotype.Service;

@Service
public class PricingServiceImpl implements PricingService {

    public double calculatePrice(RideEstimateDTO request) {
        double km = request.getEstimatedDistance() / 1000.0;

        double basePrice = switch (request.getVehicleType()) {
            case STANDARD -> 2.5;
            case LUXURY -> 4.5;
            case VAN -> 5.5;
        };

        double total = basePrice + (km * 1.20);

        return Math.max(4.0, Math.ceil(total * 2) / 2.0);
    }
}
