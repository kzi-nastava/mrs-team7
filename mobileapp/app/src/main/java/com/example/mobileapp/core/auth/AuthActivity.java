package com.example.mobileapp.core.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mobileapp.AdminMainActivity;
import com.example.mobileapp.DriverMainActivity;
import com.example.mobileapp.R;
import com.example.mobileapp.UserMainActivity;
import com.example.mobileapp.core.api.AuthApi;
import com.example.mobileapp.core.api.dto.LoginResponse;
import com.example.mobileapp.core.network.ApiClient;
import com.example.mobileapp.features.shared.map.MapFragment;
import com.example.mobileapp.features.shared.models.enums.UserRole;
import com.example.mobileapp.features.shared.repositories.UserRepository;

public class AuthActivity extends AppCompatActivity {

    private Button btnLogin;
    private Button btnRegister;
    private boolean isCheckingUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isCheckingUser = true;
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        splashScreen.setKeepOnScreenCondition(() -> isCheckingUser);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        handleIntent(getIntent());
        UserRepository.getInstance().getCurrentUser().observe(this, user -> {
            if (user != null) {
                if (user.getRole() == UserRole.ADMIN) {
                    goToAdminMain();
                } else if (user.getRole() == UserRole.DRIVER) {
                    goToDriverMain();
                } else {
                    goToPassengerMain();
                }
            }
            isCheckingUser = false;
        });

        // --- Apply status bar inset to the public header (so it sits below the status bar) ---
        View header = findViewById(R.id.public_header);
        ViewCompat.setOnApplyWindowInsetsListener(header, (v, insets) -> {
            int topInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;

            // Add the top inset as padding (keeps original padding)
            v.setPadding(
                    v.getPaddingLeft(),
                    topInset,
                    v.getPaddingRight(),
                    v.getPaddingBottom()
            );

            return insets;
        });

        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);

        // Default screen: Map (unregistered home)
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_view, new MapFragment())
                    .commit();
        }

        // Navigate to Login
        btnLogin.setOnClickListener(v -> getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_view, new LoginFragment())
                .addToBackStack(null)
                .commit()
        );

        // Navigate to Register
        btnRegister.setOnClickListener(v -> getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_view, new RegisterFragment())
                .addToBackStack(null)
                .commit()
        );

        // Clicking the brand always returns to the Map (home) and clears back stack
        findViewById(R.id.brand_container).setOnClickListener(v -> {
            getSupportFragmentManager().popBackStack(
                    null,
                    androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
            );

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_view, new MapFragment())
                    .commit();
        });

    }
    private void handleIntent(Intent intent) {
        Uri uri = intent.getData();
        if (uri != null && "/activate".equals(uri.getPath())) {
            String token = uri.getQueryParameter("token");
            if (token != null) {
                activateAccount(token);
            }
        }
    }
    private void activateAccount(String token) {
        AuthApi api = ApiClient.get().create(AuthApi.class);
        api.activate(token).enqueue(new retrofit2.Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<LoginResponse> call,
                                   @NonNull retrofit2.Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AuthActivity.this,
                            "Email verified successfully! Please log in.",
                            Toast.LENGTH_LONG).show();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container_view, new LoginFragment())
                            .commit();
                } else {
                    Toast.makeText(AuthActivity.this,
                            "Verification failed. Link may be expired.",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<LoginResponse> call,
                                  @NonNull Throwable t) {
                Toast.makeText(AuthActivity.this,
                        "Connection error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
    private void goToDriverMain() {
        Intent intent = new Intent(this, DriverMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void goToPassengerMain() {
        Intent intent = new Intent(this, UserMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void goToAdminMain() {
        Intent intent = new Intent(this, AdminMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
