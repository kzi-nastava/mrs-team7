package com.example.mobileapp.features.passenger.rideBooking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobileapp.R;
import com.example.mobileapp.features.shared.input.LocationSearchInputFragment;
import com.example.mobileapp.features.shared.map.MapFragment;
import com.example.mobileapp.features.shared.profile.ChangePasswordDialogFragment;
import com.example.mobileapp.features.shared.profile.ProfileViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.checkbox.MaterialCheckBox;

public class RideBookingFragment extends Fragment {

    private FrameLayout bottomSheet, successOverlay;
    private LinearLayout sheetContent, step1, step2, step3, stopsContainer, passengersContainer;
    private EditText etFrom, inputFavoriteRouteName;
    private AppCompatImageButton btnAddStop, btnAddPassenger;
    private ImageView btnFromIcon, profileImage;
    private TextView scheduledError, userEmail, successMessage;
    private AutoCompleteTextView actvFavoriteRoute, actvVehicleType;
    private AppCompatButton btnStep1Next, btnStep1Cancel, btnStep2Next, btnStep2Back, btnSaveRoute,
            btnStep3Book, btnStep3Back, btnSuccessExit;

    private CheckBox checkboxPets;
    private MaterialCheckBox checkboxInfants;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_ride_booking, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);

        if (getChildFragmentManager().findFragmentById(R.id.mapContainer) == null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mapContainer, new MapFragment())
                    .commit();
        }

        LocationSearchInputFragment pickupSearch = new LocationSearchInputFragment();
        LocationSearchInputFragment destSearch = new LocationSearchInputFragment();

        getChildFragmentManager().beginTransaction()
                .replace(R.id.pickup_container, pickupSearch)
                .replace(R.id.dest_container, destSearch)
                .commit();
    }

    private void initViews(View view) {
        // Root views
        bottomSheet = view.findViewById(R.id.bottom_sheet);
        sheetContent = view.findViewById(R.id.sheet_content);

        // Step containers
        step1 = view.findViewById(R.id.step1);
        step2 = view.findViewById(R.id.step2);
        step3 = view.findViewById(R.id.step3);

        // Step 1
        stopsContainer = view.findViewById(R.id.stops_container);
        btnAddStop = view.findViewById(R.id.btn_add_stop);
        btnFromIcon = view.findViewById(R.id.btn_from_icon);
        etFrom = view.findViewById(R.id.et_from);
        scheduledError = view.findViewById(R.id.scheduled_error);
        actvFavoriteRoute = view.findViewById(R.id.actvFavoriteRoute);
        btnStep1Next = view.findViewById(R.id.btn_step1_next);
        btnStep1Cancel = view.findViewById(R.id.btn_step1_cancel);

        // Step 2
        profileImage = view.findViewById(R.id.profile_image);
        userEmail = view.findViewById(R.id.user_email);
        passengersContainer = view.findViewById(R.id.passengers_container);
        btnAddPassenger = view.findViewById(R.id.btn_add_passenger);
        btnStep2Next = view.findViewById(R.id.btn_step2_next);
        btnStep2Back = view.findViewById(R.id.btn_step2_back);

        // Step 3
        checkboxPets = view.findViewById(R.id.checkbox_pets);
        checkboxInfants = view.findViewById(R.id.checkbox_infants);
        actvVehicleType = view.findViewById(R.id.actvVehicleType);
        inputFavoriteRouteName = view.findViewById(R.id.input_favorite_route_name);
        btnSaveRoute = view.findViewById(R.id.btn_save_route);
        btnStep3Book = view.findViewById(R.id.btn_step3_book);
        btnStep3Back = view.findViewById(R.id.btn_step3_back);

        // Success overlay
        successOverlay = view.findViewById(R.id.success_overlay);
        successMessage = view.findViewById(R.id.success_message);
        btnSuccessExit = view.findViewById(R.id.btn_success_exit);
    }
}
