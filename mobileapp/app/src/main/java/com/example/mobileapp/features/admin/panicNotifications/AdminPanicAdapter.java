package com.example.mobileapp.features.admin.panicNotifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileapp.R;
import com.example.mobileapp.features.shared.api.dto.PanicNotificationDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdminPanicAdapter extends RecyclerView.Adapter<AdminPanicAdapter.PanicViewHolder> {
    private final List<PanicNotificationDto> panics = new ArrayList<>();
    private Context context;
    private OnResolveClickListener resolveClickListener;
    public interface OnResolveClickListener {
        void onResolveClick(PanicNotificationDto panic);
    }

    public void setOnResolveClickListener(OnResolveClickListener listener) {
        this.resolveClickListener = listener;
    }
    public void setPanics(List<PanicNotificationDto> newPanics) {
        this.panics.clear();
        if (newPanics != null) this.panics.addAll(newPanics);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PanicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_admin_panic, parent, false);
        return new PanicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PanicViewHolder holder, int position) {
        PanicNotificationDto panic = panics.get(position);

        holder.tvRideNumber.setText("Ride #" + panic.getRideId());
        holder.tvUserEmail.setText(panic.getActivatedBy());
        holder.tvTypeLabel.setText("Type: ");
        holder.tvType.setText(panic.getUserType().toString());
        holder.tvTime.setText(formatTimestamp(panic.getTimestamp()));
        holder.btnResolve.setOnClickListener(v -> {
            if (resolveClickListener != null) {
                resolvePanic(holder, panic, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return panics.size();
    }

    private void resolvePanic(PanicViewHolder holder, PanicNotificationDto panic, int position) {
        new androidx.appcompat.app.AlertDialog.Builder(context)
                .setTitle("Resolve Panic")
                .setMessage("Resolve panic for Ride #" + panic.getRideId() + "?")
                .setPositiveButton("Resolve", (dialog, which) -> {
                    if (resolveClickListener != null) {
                        resolveClickListener.onResolveClick(panic);
                    }
                    Toast.makeText(context, "Panic resolved", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private String formatTimestamp(Object timestamp) {
        if (timestamp == null) return "Time Error";

        try {
            if (timestamp instanceof LocalDateTime) {
                return DateTimeFormatter.ofPattern("M/dd/yy, h:mm a", Locale.getDefault())
                        .format((LocalDateTime) timestamp);
            } else {
                String s = timestamp.toString();
                int dot = s.indexOf('.');
                if (dot != -1) s = s.substring(0, dot);
                String[] parts = s.split("T");
                if (parts.length == 2) {
                    String[] ymd = parts[0].split("-");
                    String[] hms = parts[1].split(":");
                    if (ymd.length == 3 && hms.length >= 2) {
                        return String.format(Locale.US, "%s/%s/%s, %s:%s %s",
                                ymd[1], ymd[2], ymd[0].substring(2),
                                hms[0], hms[1], hms[0].compareTo("12") >= 0 ? "PM" : "AM");
                    }
                }
            }
        } catch (Exception ignored) {}
        return "Time Error";
    }

    static class PanicViewHolder extends RecyclerView.ViewHolder {
        TextView tvRideNumber, tvUserEmail, tvTypeLabel, tvType, tvTime, tvActive;
        Button btnResolve;
        ImageView ivAlertIcon;

        PanicViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRideNumber = itemView.findViewById(R.id.tvRideNumber);
            tvUserEmail = itemView.findViewById(R.id.tvUserEmail);
            tvTypeLabel = itemView.findViewById(R.id.tvTypeLabel);
            tvType = itemView.findViewById(R.id.tvType);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvActive = itemView.findViewById(R.id.tvActive);
            btnResolve = itemView.findViewById(R.id.btnResolve);
            ivAlertIcon = itemView.findViewById(R.id.ivAlertIcon);
        }
    }
}
