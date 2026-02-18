package com.example.mobileapp.features.shared.api.dto;

import com.example.mobileapp.features.shared.models.enums.RideStatus;
import com.example.mobileapp.features.shared.models.enums.VehicleType;

import java.time.LocalDateTime;
import java.util.List;

public class RideDto {
    public Integer id;
    public String creatorEmail;
    public String driverEmail;
    public RideStatus status;
    public LocationDto startLocation;
    public LocationDto endLocation;
    public List<LocationDto> waypoints;
    public List<PassengerDto> passengers;
    public List<String> passengerEmails;
    public VehicleType vehicleType;
    public double basePrice;
    public boolean panicActivated;
    public String cancelledBy;
    public LocalDateTime scheduledTime;
    public LocalDateTime estimatedStartTime;

    public boolean babyFriendly;
    public boolean petsFriendly;

    public Integer vehicleId;
    public String vehicleModel;
    public String vehicleLicensePlate;
    public LocalDateTime arrivedAtPickupTime;
}
