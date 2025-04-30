package com.example.moneymate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moneymate.models.LenderLoginRequest;
import com.example.moneymate.models.LoginResponse;
import com.example.moneymate.network.ApiClient;
import com.example.moneymate.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenticateLenderActivity extends AppCompatActivity {

    EditText privateKeyEditText, passwordEditText;
    Button loginButton;
    TextView textCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate_lender);

        privateKeyEditText = findViewById(R.id.privateKey);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.btnLogin);
        textCreateAccount = findViewById(R.id.textCreateAccount);

        textCreateAccount.setOnClickListener(view -> {
            Intent intent = new Intent(AuthenticateLenderActivity.this, RegisterLenderActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(view -> {
            String privateKey = privateKeyEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (privateKey.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            authenticateLender(privateKey, password);
        });
    }

    private void authenticateLender(String privateKey, String password) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        LenderLoginRequest loginRequest = new LenderLoginRequest(privateKey, password);

        Call<LoginResponse> call = apiService.authenticateLender(loginRequest);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse.isSuccess()) {
                        Toast.makeText(AuthenticateLenderActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AuthenticateLenderActivity.this, LenderHomeActivity.class);
                        intent.putExtra("lender_id", loginResponse.getLenderId());
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(AuthenticateLenderActivity.this, loginResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.e("LOGIN_FAIL", "Code: " + response.code());
                    try {
                        if (response.errorBody() != null) {
                            Log.e("LOGIN_FAIL", "Error: " + response.errorBody().string());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(AuthenticateLenderActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(AuthenticateLenderActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("LENDER_LOGIN_ERROR", "onFailure: ", t);
            }
        });
    }
    }
