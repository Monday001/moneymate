package com.example.moneymate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moneymate.models.LoginRequest;
import com.example.moneymate.models.LoginResponse;
import com.example.moneymate.network.ApiClient;
import com.example.moneymate.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigninActivity extends AppCompatActivity {

    private EditText editUsername, editPassword;
    private Button buttonSignIn;
    private TextView textSignUp, forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        textSignUp = findViewById(R.id.textSignUp);
        forgotPassword = findViewById(R.id.forgotPassword);

        buttonSignIn.setOnClickListener(view -> {
            String input = editUsername.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (input.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Try to login as lender first (private_key field)
            LoginRequest loginRequest = new LoginRequest(null, password, input); // private_key set

            ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
            Call<LoginResponse> call = apiService.loginUser(loginRequest);

            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        LoginResponse loginResponse = response.body();
                        if (loginResponse.isSuccess()) {
                            saveUserAndProceed(loginResponse);
                        } else {
                            // If private_key failed, try as a normal user using username
                            tryUserLogin(input, password);
                        }
                    } else {
                        tryUserLogin(input, password);
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(SigninActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        textSignUp.setOnClickListener(v -> startActivity(new Intent(SigninActivity.this, SignupActivity.class)));
        forgotPassword.setOnClickListener(v -> startActivity(new Intent(SigninActivity.this, PassResetActivity.class)));
    }

    private void tryUserLogin(String username, String password) {
        LoginRequest loginRequest = new LoginRequest(username, password, null); // username set

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<LoginResponse> call = apiService.loginUser(loginRequest);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse.isSuccess()) {
                        saveUserAndProceed(loginResponse);
                    } else {
                        Toast.makeText(SigninActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SigninActivity.this, "Login failed: server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(SigninActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserAndProceed(LoginResponse loginResponse) {
        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", loginResponse.getUsername());
        editor.putString("user_type", loginResponse.getUserType());

        if ("user".equals(loginResponse.getUserType())) {
            editor.putString("borrower_id", loginResponse.getBorrowerId());
        } else if ("lender".equals(loginResponse.getUserType())) {
            editor.putString("lender_id", loginResponse.getLenderId());
        }

        editor.apply();

        Toast.makeText(SigninActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(SigninActivity.this, SelectionActivity.class));
        finish();
    }
}
