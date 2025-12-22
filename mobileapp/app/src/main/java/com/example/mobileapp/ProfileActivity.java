package com.example.mobileapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class ProfileActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profile_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ((EditText)findViewById(R.id.et_first_name)).setText("Andrew");
        ((EditText)findViewById(R.id.et_last_name)).setText("Wilson");
        ((EditText)findViewById(R.id.et_email)).setText("andrewwilson@email.com");
        ((EditText)findViewById(R.id.et_address)).setText("Bradford");
        ((EditText)findViewById(R.id.et_phone)).setText("065 123 1233");
        ((TextView)findViewById(R.id.tv_full_name)).setText("Andrew Wilson");
        ((TextView)findViewById(R.id.tv_email)).setText("andrewwilson@email.com");


        drawerLayout = findViewById(R.id.profile_main);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        View headerView = navigationView.getHeaderView(0);

        AppCompatImageButton closeButton = headerView.findViewById(R.id.close_drawer_btn);

        closeButton.setOnClickListener(v -> {
            drawerLayout.closeDrawers();
        });


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Drawer icon
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Navigation
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                if (id == R.id.nav_dashboard) {
                    return true;
                } else if (id == R.id.nav_book_ride) {
                    return true;
                } else if (id == R.id.nav_booked_rides) {
                    return true;
                } else if (id == R.id.nav_ride_history) {
                    return true;
                } else if (id == R.id.nav_reports) {
                    return true;
                } else if (id == R.id.nav_support) {
                    return true;
                } else if (id == R.id.nav_sign_out) {
                    return true;
                }
                return false;
            }
        });

    }

}