package com.example.mobileapp.features.admin.services;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mobileapp.core.network.ApiClient;
import com.example.mobileapp.features.shared.api.AdminApi;
import com.example.mobileapp.features.shared.api.dto.PanicNotificationDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminPanicService {
    private final MutableLiveData<List<PanicNotificationDto>> _panics = new MutableLiveData<>();
    public LiveData<List<PanicNotificationDto>> panics() { return _panics; }

    private final AdminApi api;
    private final SharedPreferences prefs;

    public AdminPanicService(Context context) {
        prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
        api = ApiClient.get().create(AdminApi.class);
    }

    public void fetchPanics() {
        String token = prefs.getString("jwt", null);
        if (token == null) return;

        api.getPanicNotifications("Bearer " + token)
                .enqueue(new Callback<List<PanicNotificationDto>>() {
                    @Override
                    public void onResponse(Call<List<PanicNotificationDto>> call, Response<List<PanicNotificationDto>> response) {
                        if (response.isSuccessful()) {
                            _panics.setValue(response.body());
                        }
                    }

                    @Override public void onFailure(Call<List<PanicNotificationDto>> call, Throwable t) {}
                });
    }

    public void resolvePanic(Integer rideId) {
        String token = prefs.getString("jwt", null);
        api.resolvePanic("Bearer " + token, rideId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            fetchPanics();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull retrofit2.Call<Void> call,
                                          @NonNull Throwable t) {
                    }
                });
    }

}

