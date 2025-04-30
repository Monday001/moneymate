package com.example.moneymate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moneymate.models.VerifyOtpRequest;
import com.example.moneymate.models.VerifyOtpResponse;
import com.example.moneymate.network.ApiClient;
import com.example.moneymate.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActivity extends AppCompatActivity {

    private EditText otp1, otp2, otp3, otp4, otp5;

    private void setupOtpInputs() {
        otp1.addTextChangedListener(new OtpTextWatcher(otp1, otp2, null));
        otp2.addTextChangedListener(new OtpTextWatcher(otp2, otp3, otp1));
        otp3.addTextChangedListener(new OtpTextWatcher(otp3, otp4, otp2));
        otp4.addTextChangedListener(new OtpTextWatcher(otp4, otp5, otp3));
        otp5.addTextChangedListener(new OtpTextWatcher(otp5, null, otp4));
    }


    private Button verifyButton;
    private String phonenumber; // Replace with actual email from intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        otp1 = findViewById(R.id.otpDigit1);
        otp2 = findViewById(R.id.otpDigit2);
        otp3 = findViewById(R.id.otpDigit3);
        otp4 = findViewById(R.id.otpDigit4);
        otp5 = findViewById(R.id.otpDigit5);
        verifyButton = findViewById(R.id.verifyCodeButton);

        setupOtpInputs();

        // Find and set up back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // ✅ Properly retrieve phone/email from Intent
        Intent intent = getIntent();
        phonenumber = intent.getStringExtra("phonenumber"); // or use "phone" if you pass phone number

        if (phonenumber == null || phonenumber.isEmpty()) {
            Toast.makeText(this, "Missing email/phone from previous screen", Toast.LENGTH_SHORT).show();
            finish(); // Stop activity if no data is passed
        }
        verifyButton.setOnClickListener(v -> verifyOtp());
    }

    private void verifyOtp() {
        String otpCode = otp1.getText().toString().trim() +
                otp2.getText().toString().trim() +
                otp3.getText().toString().trim() +
                otp4.getText().toString().trim() +
                otp5.getText().toString().trim();

        if (otp1.getText().toString().isEmpty() ||
                otp2.getText().toString().isEmpty() ||
                otp3.getText().toString().isEmpty() ||
                otp4.getText().toString().isEmpty() ||
                otp5.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter all 5 OTP digits", Toast.LENGTH_SHORT).show();
            return;
        }

        if (otpCode.length() < 5) {
            Toast.makeText(this, "Enter all 5 digits of the OTP", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Verifying OTP...");
        dialog.setCancelable(false);
        dialog.show();

        VerifyOtpRequest request = new VerifyOtpRequest(phonenumber, otpCode);

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<VerifyOtpResponse> call = apiService.verifyOtp(request);

        call.enqueue(new Callback<VerifyOtpResponse>() {
            @Override
            public void onResponse(Call<VerifyOtpResponse> call, Response<VerifyOtpResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        Toast.makeText(OtpActivity.this, "OTP Verified!", Toast.LENGTH_SHORT).show();

                        // Navigate to NewPasswordActivity
                        Intent intent = new Intent(OtpActivity.this, NewPasswordActivity.class);
                        intent.putExtra("phonenumber", phonenumber); // pass email to next activity
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(OtpActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OtpActivity.this, "Invalid server response", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<VerifyOtpResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(OtpActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class OtpTextWatcher implements android.text.TextWatcher {

        private final EditText currentView;
        private final EditText nextView;
        private final EditText previousView;

        public OtpTextWatcher(EditText currentView, EditText nextView, EditText previousView) {
            this.currentView = currentView;
            this.nextView = nextView;
            this.previousView = previousView;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(android.text.Editable s) {
            if (s.length() == 1 && nextView != null) {
                nextView.requestFocus();
            } else if (s.length() == 0 && previousView != null) {
                previousView.requestFocus();
            }
        }
    }

}
