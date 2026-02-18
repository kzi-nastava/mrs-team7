package com.example.mobileapp.features.shared.api;

import com.example.mobileapp.features.shared.api.dto.DriverListItemDto;
import com.example.mobileapp.features.shared.api.dto.DriverRideDto;
import com.example.mobileapp.features.shared.api.dto.PanicNotificationDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AdminApi {

    @GET("api/admin/drivers/all-with-status")
    Call<List<DriverListItemDto>> getAllDrivers(
            @Header("Authorization") String authHeader
    );

    @GET("api/admin/drivers/{driverEmail}/rides")
    Call<List<DriverRideDto>> getDriverRides(
            @Header("Authorization") String authHeader,
            @Path("driverEmail") String driverEmail
    );
    @GET("api/admin/panic-notifications")
    Call<List<PanicNotificationDto>> getPanicNotifications(
            @Header("Authorization") String authHeader
    );
    @PUT("api/admin/panic-notifications/{rideId}/resolve")
    Call<Void> resolvePanic(
            @Header("Authorization") String bearerToken,
            @Path("rideId") Integer rideId
    );
}