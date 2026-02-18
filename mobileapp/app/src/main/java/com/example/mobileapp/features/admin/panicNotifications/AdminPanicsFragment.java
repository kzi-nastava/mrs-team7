package com.example.mobileapp.features.admin.panicNotifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileapp.R;
import com.example.mobileapp.features.admin.services.AdminPanicService;
import com.example.mobileapp.features.shared.api.dto.PanicNotificationDto;

import java.util.List;

public class AdminPanicsFragment extends Fragment {

    private TextView tvActiveCount, tvAlertCount;
    private RecyclerView rvPanicAlerts;
    private LinearLayout layoutEmptyState;
    private AdminPanicAdapter adapter;
    private AdminPanicService service;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_panics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindViews(view);
        setupRecyclerView();
        setupService();
        fetchPanics();
    }

    private void bindViews(View view) {
        tvActiveCount = view.findViewById(R.id.tvActiveCount);
        tvAlertCount = view.findViewById(R.id.tvAlertCount);
        rvPanicAlerts = view.findViewById(R.id.rvPanicAlerts);
        layoutEmptyState = view.findViewById(R.id.layoutEmptyState);
    }

    private void setupRecyclerView() {
        adapter = new AdminPanicAdapter();
        rvPanicAlerts.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvPanicAlerts.setAdapter(adapter);
        adapter.setOnResolveClickListener(panic -> {
            service.resolvePanic(panic.getRideId());
        });
    }

    private void setupService() {
        service = new AdminPanicService(requireContext());
        service.panics().observe(getViewLifecycleOwner(), this::onPanicsUpdated);
    }

    public void fetchPanics() {
        service.fetchPanics();
    }

    private void onPanicsUpdated(List<PanicNotificationDto> panics) {
        adapter.setPanics(panics);
        updateHeaderCounts(panics);
    }

    private void updateHeaderCounts(List<PanicNotificationDto> panics) {
        int activeCount = panics.size();
        tvActiveCount.setText(activeCount + " active");
        tvAlertCount.setText(String.valueOf(activeCount));

        layoutEmptyState.setVisibility(activeCount == 0 ? View.VISIBLE : View.GONE);
        rvPanicAlerts.setVisibility(activeCount > 0 ? View.VISIBLE : View.GONE);
    }
}



