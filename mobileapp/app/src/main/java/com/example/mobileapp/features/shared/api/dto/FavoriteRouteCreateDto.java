package com.example.mobileapp.features.shared.api.dto;

import com.example.mobileapp.features.shared.models.enums.VehicleType;

import java.util.List;

public class FavoriteRouteCreateDto {
    public String name;

    public LocationDto startLocation;

    public LocationDto endLocation;

    public List<LocationDto> waypoints;

    public VehicleType vehicleType;

    public boolean babyFriendly;
    public boolean petsFriendly;
}
