package com.example.mobileapp.features.passenger.currentride;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mobileapp.core.network.ApiClient;
import com.example.mobileapp.features.shared.api.RidesApi;
import com.example.mobileapp.features.shared.api.dto.PassengerRideDto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengerCurrentRideService {

    private final RidesApi ridesApi;
    private final SharedPreferences prefs;

    private final MutableLiveData<PassengerRideDto> currentRide = new MutableLiveData<>(null);

    public PassengerCurrentRideService(@NonNull Context context) {
        this.ridesApi = ApiClient.get().create(RidesApi.class);
        this.prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
    }

    public LiveData<PassengerRideDto> currentRide() {
        return currentRide;
    }

    public void fetchCurrentRide() {
        String token = prefs.getString("jwt", null);
        if (token == null || token.isEmpty()) {
            currentRide.postValue(null);
            return;
        }

        ridesApi.getPassengerCurrentInProgress("Bearer " + token).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<PassengerRideDto> call,
                                   @NonNull Response<PassengerRideDto> response) {

                if (!response.isSuccessful() || response.body() == null) {
                    currentRide.postValue(null);
                    return;
                }

                PassengerRideDto r = response.body();

                if (r == null || r.status == null || !"IN_PROGRESS".equals(r.status)) {
                    currentRide.postValue(null);
                    return;
                }

                currentRide.postValue(r);
            }

            @Override
            public void onFailure(@NonNull Call<PassengerRideDto> call, @NonNull Throwable t) {
                currentRide.postValue(null);
            }
        });
    }
}
