package com.uberplus.backend.service;

import com.uberplus.backend.dto.ride.RideEstimateDTO;

public interface PricingService {
    double calculatePrice(RideEstimateDTO request);
}
