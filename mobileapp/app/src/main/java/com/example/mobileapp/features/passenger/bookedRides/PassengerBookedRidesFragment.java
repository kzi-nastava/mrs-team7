package com.example.mobileapp.features.passenger.bookedRides;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileapp.R;
import com.example.mobileapp.core.network.ApiClient;
import com.example.mobileapp.features.shared.api.RidesApi;
import com.example.mobileapp.features.shared.api.dto.RideCancellationDto;
import com.example.mobileapp.features.shared.api.dto.RideDto;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengerBookedRidesFragment extends Fragment {

    private RecyclerView rvBookedRides;
    private View emptyState;
    private PassengerBookedRidesAdapter adapter;
    private RidesApi ridesApi;
    private SharedPreferences prefs;
    private List<RideDto> currentRides = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_passenger_booked_rides, container, false);

        rvBookedRides = v.findViewById(R.id.rvPassengerBookedRides);
        emptyState = v.findViewById(R.id.emptyState);

        prefs = requireContext().getSharedPreferences("auth", android.content.Context.MODE_PRIVATE);
        ridesApi = ApiClient.get().create(RidesApi.class);

        setupRecyclerView();
        fetchBookedRides();

        return v;
    }

    private void setupRecyclerView() {
        rvBookedRides.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new PassengerBookedRidesAdapter(new ArrayList<>());
        rvBookedRides.setAdapter(adapter);

        adapter.setOnCancelClickListener(this::cancelRide);
    }

    private void fetchBookedRides() {
        String token = prefs.getString("jwt", null);
        if (token == null || token.isEmpty()) {
            showEmptyState();
            return;
        }

        ridesApi.getPassengerBookedRides("Bearer " + token).enqueue(new Callback<List<RideDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<RideDto>> call,
                                   @NonNull Response<List<RideDto>> response) {
                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null) {
                    renderBookedRides(response.body());
                } else {
                    showEmptyState();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<RideDto>> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                showEmptyState();
                android.widget.Toast.makeText(requireContext(),
                        "Failed to load rides",
                        android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void renderBookedRides(@NonNull List<RideDto> list) {
        List<PassengerBookedRidesAdapter.PassengerBookedRide> mapped = new ArrayList<>();
        currentRides = list;

        for (RideDto r : list) {
            if (r == null) continue;

            String date = formatDateFromIso(r.estimatedStartTime.toString());
            String time = formatTimeFromIso(r.estimatedStartTime.toString());
            String from = r.startLocation != null ? safe(r.startLocation.getAddress()) : "";
            String to = r.endLocation != null ? safe(r.endLocation.getAddress()) : "";
            String status = "Scheduled";

            mapped.add(new PassengerBookedRidesAdapter.PassengerBookedRide(
                    r.id,
                    date,
                    time,
                    from,
                    to,
                    status
            ));
        }

        adapter.setItems(mapped);

        if (mapped.isEmpty()) {
            showEmptyState();
        } else {
            rvBookedRides.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
        }
    }

    private void showEmptyState() {
        rvBookedRides.setVisibility(View.GONE);
        emptyState.setVisibility(View.VISIBLE);
    }

    private void cancelRide(int rideId) {
        String token = prefs.getString("jwt", null);
        if (token == null || token.isEmpty()) {
            android.widget.Toast.makeText(requireContext(),
                    "Authentication required",
                    android.widget.Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = prefs.getInt("userId", -1);
        if (userId == -1) {
            android.widget.Toast.makeText(requireContext(),
                    "User not found",
                    android.widget.Toast.LENGTH_SHORT).show();
            return;
        }
        if (!canCancelRide(rideId)) {
            new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Cannot Cancel")
                    .setMessage("Rides can only be cancelled up to 10 minutes before the scheduled time.")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }
        final android.widget.EditText input = new android.widget.EditText(requireContext());
        input.setHint("Enter cancellation reason");
        input.setMinLines(3);
        input.setMaxLines(5);
        input.setGravity(android.view.Gravity.TOP | android.view.Gravity.START);
        input.setPadding(50, 40, 50, 40);

        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Cancel Ride")
                .setMessage("Please provide a reason for cancelling this ride:")
                .setView(input)
                .setPositiveButton("Cancel Ride", (dialog, which) -> {
                    String reason = input.getText().toString().trim();

                    if (reason.isEmpty()) {
                        android.widget.Toast.makeText(requireContext(),
                                "Please provide a cancellation reason",
                                android.widget.Toast.LENGTH_SHORT).show();
                        return;
                    }

                    RideCancellationDto cancellationDto = new RideCancellationDto(userId, reason);

                    ridesApi.cancelRide("Bearer " + token, rideId, cancellationDto)
                            .enqueue(new Callback<RideDto>() {
                                @Override
                                public void onResponse(@NonNull Call<RideDto> call,
                                                       @NonNull Response<RideDto> response) {
                                    if (!isAdded()) return;

                                    if (response.isSuccessful()) {
                                        android.widget.Toast.makeText(requireContext(),
                                                "Ride cancelled successfully",
                                                android.widget.Toast.LENGTH_LONG).show();
                                        fetchBookedRides();
                                    } else {
                                        android.widget.Toast.makeText(requireContext(),
                                                "Failed to cancel ride",
                                                android.widget.Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<RideDto> call,
                                                      @NonNull Throwable t) {
                                    if (!isAdded()) return;

                                    android.widget.Toast.makeText(requireContext(),
                                            "Network error. Please try again.",
                                            android.widget.Toast.LENGTH_LONG).show();
                                }
                            });
                })
                .setNegativeButton("Keep Ride", null)
                .show();
    }

    private String safe(String s) {
        return s != null ? s : "";
    }

    private String formatDateFromIso(String iso) {
        if (iso == null) return "--.--.-";
        try {
            String s = iso;
            int dot = s.indexOf('.');
            if (dot != -1) s = s.substring(0, dot);

            String[] parts = s.split("T");
            if (parts.length == 0) return "--.--.-";

            String[] ymd = parts[0].split("-");
            if (ymd.length != 3) return "--.--.-";

            return ymd[2] + "." + ymd[1] + ".";
        } catch (Exception e) {
            return "--.--.-";
        }
    }

    private String formatTimeFromIso(String iso) {
        if (iso == null) return "--:--";
        try {
            String s = iso;
            int dot = s.indexOf('.');
            if (dot != -1) s = s.substring(0, dot);

            String[] parts = s.split("T");
            if (parts.length != 2) return "--:--";

            String[] hms = parts[1].split(":");
            if (hms.length < 2) return "--:--";

            return hms[0] + ":" + hms[1];
        } catch (Exception e) {
            return "--:--";
        }
    }
    private boolean canCancelRide(int rideId) {
        RideDto ride = null;
        for (RideDto r : currentRides) {
            if (r != null && r.id == rideId) {
                ride = r;
                break;
            }
        }

        if (ride == null || ride.scheduledTime == null) {
            return false;
        }

        try {
            // Parse scheduled time
            String iso = ride.estimatedStartTime.toString();
            int dot = iso.indexOf('.');
            if (dot != -1) iso = iso.substring(0, dot);

            String[] parts = iso.split("T");
            if (parts.length != 2) return false;

            String[] ymd = parts[0].split("-");
            String[] hms = parts[1].split(":");
            if (ymd.length != 3 || hms.length < 2) return false;

            int year = Integer.parseInt(ymd[0]);
            int month = Integer.parseInt(ymd[1]) - 1; // Calendar months are 0-based
            int day = Integer.parseInt(ymd[2]);
            int hour = Integer.parseInt(hms[0]);
            int minute = Integer.parseInt(hms[1]);
            int second = hms.length >= 3 ? Integer.parseInt(hms[2]) : 0;

            // Create calendar for scheduled time
            java.util.Calendar scheduledCal = java.util.Calendar.getInstance();
            scheduledCal.set(year, month, day, hour, minute, second);
            scheduledCal.set(java.util.Calendar.MILLISECOND, 0);

            // Get current time
            java.util.Calendar nowCal = java.util.Calendar.getInstance();

            // Calculate difference in milliseconds
            long diffMillis = scheduledCal.getTimeInMillis() - nowCal.getTimeInMillis();
            long diffMinutes = diffMillis / (60 * 1000);

            // Can cancel if more than 10 minutes before scheduled time
            return diffMinutes > 10;

        } catch (Exception e) {
            return false;
        }
    }
}

