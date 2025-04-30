package com.example.moneymate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moneymate.models.SendOtpRequest;
import com.example.moneymate.models.SendOtpResponse;
import com.example.moneymate.network.ApiClient;
import com.example.moneymate.network.ApiService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassResetActivity extends AppCompatActivity {

    private EditText phoneEditText, emailEditText;
    private Button sendOtpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pass_reset);

        phoneEditText = findViewById(R.id.phoneInput);
        emailEditText = findViewById(R.id.emailInput);
        sendOtpButton = findViewById(R.id.resetPasswordButton);

        sendOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOtp();
            }
        });

    }

    private void sendOtp() {
        String phonenumber = phoneEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();

        if (phonenumber.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please enter both phone number and email", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        SendOtpRequest request = new SendOtpRequest(phonenumber, email);
        Call<SendOtpResponse> call = apiService.sendOtp(request);

        call.enqueue(new Callback<SendOtpResponse>() {
            @Override
            public void onResponse(Call<SendOtpResponse> call, Response<SendOtpResponse> response) {
                Log.d("PassResetActivity", "Response code: " + response.code());
                Log.d("PassResetActivity", "Response body: " + new Gson().toJson(response.body()));

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        Toast.makeText(PassResetActivity.this, "OTP sent to your email!", Toast.LENGTH_SHORT).show();
                        // TODO: Move to OTP verification screen
                        Intent intent = new Intent(PassResetActivity.this, OtpActivity.class);
                        intent.putExtra("phonenumber", phonenumber);
                        startActivity(intent);
                        finish();  // Optional: Finish the current activity so the user can't go back to it
                    } else {
                        Toast.makeText(PassResetActivity.this, "Failed: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PassResetActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SendOtpResponse> call, Throwable t) {
                Log.e("PassResetActivity", "API call failed", t);
                Toast.makeText(PassResetActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
