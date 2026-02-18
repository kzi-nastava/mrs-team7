package com.example.mobileapp.features.passenger.rideBooking;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mobileapp.features.shared.api.dto.LocationDto;

import java.util.ArrayList;
import java.util.List;

public class RideBookingViewModel extends ViewModel {
    private final MutableLiveData<LocationDto> start = new MutableLiveData<>();
    private final MutableLiveData<LocationDto> end = new MutableLiveData<>();
    private final MutableLiveData<List<LocationDto>> waystops = new MutableLiveData<>();

    RideBookingViewModel () {}


    public LiveData<LocationDto> getStart() {
        return start;
    }

    public LiveData<LocationDto> getEnd() {
        return end;
    }

    public LiveData<List<LocationDto>> getWaystops() {
        return waystops;
    }


    public void setStart(LocationDto start) {
        this.start.setValue(start);
    }

    public void setEnd(LocationDto end) {
        this.end.setValue(end);
    }

    public void setWaystops(List<LocationDto> waystops) {
        this.waystops.setValue(waystops);
    }


    public void addWaystop(LocationDto waystop) {
        List<LocationDto> current = waystops.getValue();
        if (current == null) {
            current = new ArrayList<>();
        } else {
            current = new ArrayList<>(current);
        }
        current.add(waystop);
        waystops.setValue(current);
    }

    public void removeWaystopAt(int index) {
        List<LocationDto> current = waystops.getValue();
        if (current == null || index < 0 || index >= current.size()) return;

        List<LocationDto> updated = new ArrayList<>(current);
        updated.remove(index);
        waystops.setValue(updated);
    }

    public void updateWaystop(int index, LocationDto updatedWaystop) {
        List<LocationDto> current = waystops.getValue();
        if (current == null || index < 0 || index >= current.size()) return;

        List<LocationDto> updated = new ArrayList<>(current);
        updated.set(index, updatedWaystop);
        waystops.setValue(updated);
    }

    public void clearWaystops() {
        waystops.setValue(new ArrayList<>());
    }

}
