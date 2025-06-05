package com.example.moneymate;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moneymate.network.ApiClient;
import com.example.moneymate.network.ApiService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoanReviewActivity extends AppCompatActivity {

    TextView tvName, tvAmount, tvLoanPurpose, tvEmail, tvPhonenumber, tvIdFront, tvIdBack;
    int loanId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_review);

        tvName = findViewById(R.id.borrowerFullName);
        tvAmount = findViewById(R.id.loanAmount);
        tvLoanPurpose = findViewById(R.id.loanPurpose);
        tvPhonenumber = findViewById(R.id.borrowerPhone);
        tvEmail = findViewById(R.id.tvEmail);
        tvIdFront = findViewById(R.id.tvIdFront);
        tvIdBack = findViewById(R.id.tvIdBack);

        Button btnApprove = findViewById(R.id.btnApprove);
        Button btnDeny = findViewById(R.id.btnDeny);

        btnApprove.setOnClickListener(v -> showDisburseDialog());
        btnDeny.setOnClickListener(v -> showDenyDialog());

        // Intent data
        String name = getIntent().getStringExtra("full_name");
        String amount = getIntent().getStringExtra("amount");
        String purpose = getIntent().getStringExtra("purpose");
        String phone = getIntent().getStringExtra("phone");
        String email = getIntent().getStringExtra("email");
        String idFront = getIntent().getStringExtra("id_front");
        String idBack = getIntent().getStringExtra("id_back");
        loanId = getIntent().getIntExtra("loan_id", -1);

        if (loanId == -1) {
            Toast.makeText(this, "Error: Invalid loan ID", Toast.LENGTH_LONG).show();
            finish();
        }

        tvName.setText(name);
        tvAmount.setText("Kshs. " + amount);
        tvLoanPurpose.setText(purpose);
        tvPhonenumber.setText(phone);
        tvEmail.setText(email);
        tvIdFront.setText(idFront);
        tvIdBack.setText(idBack);
    }

    private void showDisburseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.disburse_dialog, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        Button btnDisburse = dialogView.findViewById(R.id.btnDisburse);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        btnDisburse.setOnClickListener(v -> {
            disburseLoan();
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
    }

    private void disburseLoan() {
        String amount = getIntent().getStringExtra("amount");

        Log.d("LoanDisburse", "Loan ID: " + loanId + ", Amount: " + amount);

        if (loanId == -1 || amount == null || amount.isEmpty()) {
            Toast.makeText(this, "Missing loan ID or amount", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = ApiClient.getRetrofitInstance();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<ResponseBody> call = apiService.disburseLoan(loanId, amount);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(LoanReviewActivity.this, "Loan disbursed successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(LoanReviewActivity.this, "Failed to disburse loan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(LoanReviewActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDenyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.activity_denial_dialog, null);
        builder.setView(dialogView);

        EditText editReason = dialogView.findViewById(R.id.editDenialReason);
        Button btnSubmit = dialogView.findViewById(R.id.btnSubmitDenial);
        Button btnCancel = dialogView.findViewById(R.id.btnCancelDenial);

        AlertDialog dialog = builder.create();
        dialog.show();

        btnSubmit.setOnClickListener(v -> {
            String reason = editReason.getText().toString().trim();
            if (reason.isEmpty()) {
                editReason.setError("Please provide a reason for denial");
            } else {
                denyLoan(reason);
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
    }

    private void denyLoan(String reason) {
        Retrofit retrofit = ApiClient.getRetrofitInstance();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<ResponseBody> call = apiService.denyLoan(loanId, reason);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(LoanReviewActivity.this, "Loan denied and borrower notified", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(LoanReviewActivity.this, "Failed to deny loan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(LoanReviewActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
