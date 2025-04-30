package com.example.moneymate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moneymate.models.GenericResponse;
import com.example.moneymate.models.ResetPasswordRequest;
import com.example.moneymate.network.ApiClient;
import com.example.moneymate.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPasswordActivity extends AppCompatActivity {

    private EditText newPasswordInput, confirmPasswordInput;
    private Button updatePasswordButton;
    private String email; // from intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        newPasswordInput = findViewById(R.id.newPasswordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        updatePasswordButton = findViewById(R.id.updatePasswordButton);

        email = getIntent().getStringExtra("email");

        updatePasswordButton.setOnClickListener(v -> {
            String newPass = newPasswordInput.getText().toString().trim();
            String confirmPass = confirmPasswordInput.getText().toString().trim();

            if (newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPass.equals(confirmPass)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            resetPassword(email, newPass);
        });
    }

    private void resetPassword(String email, String password) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        ResetPasswordRequest request = new ResetPasswordRequest(email, password);

        Call<GenericResponse> call = apiService.resetPassword(request);
        call.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(NewPasswordActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(NewPasswordActivity.this, SigninActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(NewPasswordActivity.this, "Failed: " + (response.body() != null ? response.body().getMessage() : "Unknown error"), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                Toast.makeText(NewPasswordActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
