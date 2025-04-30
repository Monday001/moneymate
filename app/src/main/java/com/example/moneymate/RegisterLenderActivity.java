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
import com.example.moneymate.models.Lender;
import com.example.moneymate.network.ApiClient;
import com.example.moneymate.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterLenderActivity extends AppCompatActivity {

    // Declare the EditText views for the user input
    EditText companynameEditText, licenseEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    Button registerButton;
    TextView authenticateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_lender);  // Use the XML layout

        // Initialize views by finding them from the layout
        companynameEditText = findViewById(R.id.companyname);
        licenseEditText = findViewById(R.id.license);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.ConfirmPassword);
        registerButton = findViewById(R.id.btnRegister);
        authenticateTextView = findViewById(R.id.textAuthenticate);

        // Set up the "Authenticate" link to navigate to the login page
        authenticateTextView.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterLenderActivity.this, AuthenticateLenderActivity.class);
            startActivity(intent);
            finish();
        });

        // Set up the Register button logic
        registerButton.setOnClickListener(v -> {
            String companyName = companynameEditText.getText().toString().trim();
            String license = licenseEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            // Validate the inputs
            if (companyName.isEmpty() || license.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(RegisterLenderActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterLenderActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Proceed to register the lender
            registerLender(companyName, license, email, password);
        });
    }

    // Function to handle the registration API call
    private void registerLender(String companyName, String license, String email, String password) {
        // Create a lender object with the user input data
        Lender lender = new Lender(companyName, license, email, password);

        // Use the ApiService to register the lender
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<SignupResponse> call = apiService.registerLender(lender);

        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SignupResponse signupResponse = response.body();
                    if (signupResponse.isSuccess()) {
                        Toast.makeText(RegisterLenderActivity.this, signupResponse.getMessage(), Toast.LENGTH_LONG).show();
                        // Navigate to the SignIn activity after successful registration
                        Intent intent = new Intent(RegisterLenderActivity.this, AuthenticateLenderActivity.class);
                        startActivity(intent);
                        finish(); // Close RegisterLenderActivity
                    } else {
                        Toast.makeText(RegisterLenderActivity.this, signupResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(RegisterLenderActivity.this, "Registration failed: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                Toast.makeText(RegisterLenderActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("REGISTER_LENDER_ERROR", "onFailure: ", t);
            }
        });
    }
}
