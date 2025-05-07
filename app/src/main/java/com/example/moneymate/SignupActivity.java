package com.example.moneymate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moneymate.models.SignupResponse;
import com.example.moneymate.models.User;
import com.example.moneymate.network.ApiClient;
import com.example.moneymate.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    EditText usernameEditText, phoneEditText, passwordEditText, confirmPasswordEditText;

    TextView tvSignInInstead;
    Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameEditText = findViewById(R.id.username);
        phoneEditText = findViewById(R.id.phonenumber);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.ConfirmPassword);
        signupButton = findViewById(R.id.btnSignUp);
        tvSignInInstead = findViewById(R.id.tvSignInInstead);

        tvSignInInstead.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
            startActivity(intent);
            finish();
        });

        signupButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            if (username.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check phone number format (just an example)
            if (!phone.matches("^[0-9]{10}$")) {
                Toast.makeText(SignupActivity.this, "Invalid phone number format", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check password strength (minimum 6 characters as an example)
            if (password.length() < 6) {
                Toast.makeText(SignupActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            registerUser(username, phone, password);
        });
    }

    private void registerUser(String username, String phone, String password) {
        User user = new User(username, phone, password);
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<SignupResponse> call = apiService.registerUser(user);

        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SignupResponse signupResponse = response.body();
                    if (signupResponse.isSuccess()) {
                        Toast.makeText(SignupActivity.this, signupResponse.getMessage(), Toast.LENGTH_LONG).show();

                        // Store username in SharedPreferences
                        getSharedPreferences("userPrefs", MODE_PRIVATE)
                                .edit()
                                .putString("username", user.getUsername())
                                .apply();

                        // Navigate to SignIn Activity
                        Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                        startActivity(intent);
                        finish();
                    }
                     else {
                        Toast.makeText(SignupActivity.this, signupResponse.getMessage(), Toast.LENGTH_LONG).show();
                        clearFields();
                    }
                } else {
                    Toast.makeText(SignupActivity.this, "Signup failed: " + response.message(), Toast.LENGTH_LONG).show();
                    clearFields();
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                Toast.makeText(SignupActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("SIGNUP_ERROR", "onFailure: ", t);
                clearFields();
            }
        });
    }


    private void clearFields() {
        usernameEditText.setText("");
        phoneEditText.setText("");
        passwordEditText.setText("");
        confirmPasswordEditText.setText("");
    }
}
