package com.example.mobileapp.features.shared.api.dto;

import com.example.mobileapp.features.shared.models.enums.VehicleType;

import java.time.LocalDateTime;
import java.util.List;

public class CreateRideRequestDto {
    public LocationDto startLocation;
    public LocationDto endLocation;
    public List<LocationDto> waypoints;
    public VehicleType vehicleType;
    public boolean babyFriendly;
    public boolean petFriendly;
    public List<String> linkedPassengerEmails;
    public LocalDateTime scheduledTime;
    public int estimatedDurationMinutes;
    public Double distanceKm;
}
