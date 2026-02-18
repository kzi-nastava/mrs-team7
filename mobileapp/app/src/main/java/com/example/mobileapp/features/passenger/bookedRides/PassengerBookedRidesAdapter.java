package com.example.mobileapp.features.passenger.bookedRides;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileapp.R;

import java.util.ArrayList;
import java.util.List;

public class PassengerBookedRidesAdapter extends RecyclerView.Adapter<PassengerBookedRidesAdapter.PassengerRideVH> {

    public interface OnCancelClickListener {
        void onCancelClick(int rideId);
    }

    public static final class PassengerBookedRide {
        public final int rideId;
        public final String date;
        public final String time;
        public final String from;
        public final String to;
        public final String status;

        public PassengerBookedRide(int rideId, String date, String time, String from,
                                   String to, String status) {
            this.rideId = rideId;
            this.date = date;
            this.time = time;
            this.from = from;
            this.to = to;
            this.status = status;
        }
    }

    private final List<PassengerBookedRide> items;
    private OnCancelClickListener cancelListener;

    public PassengerBookedRidesAdapter(@NonNull List<PassengerBookedRide> items) {
        this.items = new ArrayList<>(items);
    }

    public void setOnCancelClickListener(OnCancelClickListener listener) {
        this.cancelListener = listener;
    }

    public void setItems(@NonNull List<PassengerBookedRide> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PassengerRideVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_passenger_booked_ride, parent, false);
        return new PassengerRideVH(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PassengerRideVH h, int position) {
        PassengerBookedRide r = items.get(position);

        h.tvDate.setText(r.date);
        h.tvTime.setText(r.time);
        h.tvFrom.setText(r.from);
        h.tvTo.setText(r.to);
        h.tvStatus.setText(r.status);

        h.btnCancel.setOnClickListener(v -> {
            if (cancelListener != null) {
                cancelListener.onCancelClick(r.rideId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static final class PassengerRideVH extends RecyclerView.ViewHolder {
        final TextView tvDate;
        final TextView tvTime;
        final TextView tvFrom;
        final TextView tvTo;
        final TextView tvStatus;
        final TextView btnCancel;

        public PassengerRideVH(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvPassengerRideDate);
            tvTime = itemView.findViewById(R.id.tvPassengerRideTime);
            tvFrom = itemView.findViewById(R.id.tvPassengerRideFrom);
            tvTo = itemView.findViewById(R.id.tvPassengerRideTo);
            tvStatus = itemView.findViewById(R.id.tvPassengerRideStatus);
            btnCancel = itemView.findViewById(R.id.btnCancelPassengerRide);
        }
    }
}

