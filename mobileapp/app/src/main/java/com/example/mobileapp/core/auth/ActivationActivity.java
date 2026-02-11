package com.example.mobileapp.core.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileapp.R;
import com.example.mobileapp.core.api.AuthApi;
import com.example.mobileapp.core.api.dto.LoginResponse;
import com.example.mobileapp.core.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivationActivity extends AppCompatActivity{
    private ProgressBar progressBar;
    private TextView tvStatus;
    private TextView tvMessage;
    private Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);

        progressBar = findViewById(R.id.progress_bar);
        tvStatus = findViewById(R.id.tv_verification_status);
        tvMessage = findViewById(R.id.tv_verification_message);
        btnContinue = findViewById(R.id.btn_continue);
        btnContinue.setOnClickListener(v -> {
            Intent intent = new Intent(this, AuthActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        Uri uri = getIntent().getData();
        if (uri != null) {
            String token = uri.getQueryParameter("token");
            if (token != null) {
                verifyEmailWithBackend(token);
            }
        }
    }

    private void verifyEmailWithBackend(String token) {
        AuthApi authApi = ApiClient.get().create(AuthApi.class);

        Call<LoginResponse> call = authApi.activate(token);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    tvStatus.setText("Email Verified!");
                    tvMessage.setText("Your account is now active");
                    btnContinue.setVisibility(View.VISIBLE);
                } else {
                    tvStatus.setText("Verification Failed");
                    tvMessage.setText("Link may be expired or invalid");
                    btnContinue.setText("Back to Login");
                    btnContinue.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                tvStatus.setText("Connection Error");
                tvMessage.setText(t.getMessage());
                btnContinue.setText("Back to Login");
                btnContinue.setVisibility(View.VISIBLE);
            }
        });
    }
}

