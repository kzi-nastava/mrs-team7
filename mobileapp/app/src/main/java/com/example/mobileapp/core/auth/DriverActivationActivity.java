package com.example.mobileapp.core.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.mobileapp.R;
import com.example.mobileapp.core.api.AuthApi;
import com.example.mobileapp.core.api.dto.LoginResponse;
import com.example.mobileapp.core.network.ApiClient;
import com.example.mobileapp.features.shared.api.DriversApi;
import com.example.mobileapp.features.shared.api.UserApi;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverActivationActivity extends AppCompatActivity{
    private ProgressBar progressBar;
    private TextView tvStatus;
    private TextView tvMessage;
    private Button btnContinue;

    LinearLayout formContainer, loadingContainer;
    EditText etNewPassword, etConfirmPassword;
    AppCompatButton btnConfirm;
    TextView tvErrorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_activation);

        formContainer = findViewById(R.id.form_container);
        loadingContainer = findViewById(R.id.loading_container);

        etNewPassword = findViewById(R.id.et_new_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);

        btnConfirm = findViewById(R.id.btn_confirm);

        tvErrorMessage = findViewById(R.id.tv_error_message);

        progressBar = findViewById(R.id.progress_bar);
        tvStatus = findViewById(R.id.tv_verification_status);
        tvMessage = findViewById(R.id.tv_verification_message);
        btnContinue = findViewById(R.id.btn_continue);
        btnContinue.setOnClickListener(v -> {
            Intent intent = new Intent(this, AuthActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        btnConfirm.setOnClickListener(v -> {
            verifyEmailWithBackend("8abb73c3-7a6d-495e-a8b9-ceb1c8c95c8b");
        });

//        Uri uri = getIntent().getData();
//        if (uri != null) {
//            String token = uri.getQueryParameter("token");
//            if (token != null) {
//                btnConfirm.setOnClickListener(v -> {
//                    verifyEmailWithBackend(token);
//                });
//            }
//        }
    }

    private void verifyEmailWithBackend(String token) {
        String newPassword = etNewPassword.getText().toString();
        String errorMsg = validatePasswords();

        if (errorMsg != null) {
            tvErrorMessage.setText(errorMsg);
            return;
        }

        tvErrorMessage.setText(null);

        Map<String, String> body = new HashMap<>();
        body.put("password", newPassword);
        body.put("token", token);

        formContainer.setVisibility(View.GONE);
        loadingContainer.setVisibility(View.VISIBLE);

        ApiClient.get().create(DriversApi.class).activate(body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    tvStatus.setText("Password set!");
                    tvMessage.setText("Your account is now active");
                    btnContinue.setVisibility(View.VISIBLE);
                } else {
                    tvStatus.setText("Failed to set password");
                    tvMessage.setText("Link may be expired or invalid");
                    btnContinue.setText("Back to Login");
                    btnContinue.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                tvStatus.setText("Connection Error");
                tvMessage.setText(t.getMessage());
                btnContinue.setText("Back to Login");
                btnContinue.setVisibility(View.VISIBLE);
            }
        });
    }

    private String validatePasswords() {
        String newPassword = etNewPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        if (newPassword.isEmpty()) {
            return "Please enter your new password.";
        }
        if (confirmPassword.isEmpty()) {
            return "Please confirm your new password.";
        }
        if (!(newPassword.equals(confirmPassword))) {
            return "The passwords do not match.";
        }
        if (newPassword.length() < 8) {
            return "Password must be at least 8 characters.";
        }
        return null;
    }
}

