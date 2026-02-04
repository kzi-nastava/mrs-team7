package com.example.mobileapp.features.shared.api;

import com.example.mobileapp.features.shared.api.dto.DriverDto;
import com.example.mobileapp.features.shared.api.dto.UserUpdateRequestDto;
import com.example.mobileapp.features.shared.models.User;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface DriversApi {
    @GET("api/drivers/profile")
    Call<DriverDto> fetchMe();

    @Multipart
    @PUT("api/drivers/profile")
    Call<Void> updateProfile(@Part("update") UserUpdateRequestDto user,
                             @Part MultipartBody.Part profileImage);
}
