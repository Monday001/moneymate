package com.example.moneymate;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymate.adapter.PaymentHistoryAdapter;
import com.example.moneymate.models.GenericResponse;
import com.example.moneymate.models.Payment;
import com.example.moneymate.models.Repayment;
import com.example.moneymate.models.RepaymentResponse;
import com.example.moneymate.network.ApiClient;
import com.example.moneymate.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepaymentDetailsActivity extends AppCompatActivity {

    TextView companyName, amount, paidOn;
    RecyclerView recyclerView;
    Button closeButton;
    PaymentHistoryAdapter adapter;
    int loanId; // Changed from String to int

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repayment_details);

        companyName = findViewById(R.id.detail_name);
        amount = findViewById(R.id.detail_amount);
        recyclerView = findViewById(R.id.lender_detail_payment_history);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PaymentHistoryAdapter();
        recyclerView.setAdapter(adapter);

        // Convert loanId from String to int
        String loanIdString = getIntent().getStringExtra("loan_id");
        try {
            loanId = Integer.parseInt(loanIdString); // Parse the loanId to an integer
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid loan ID format", Toast.LENGTH_SHORT).show();
            finish(); // Exit activity if loanId is invalid
            return;
        }

        fetchRepaymentDetails(loanId);

        closeButton.setOnClickListener(v -> finish());

        findViewById(R.id.repayButton).setOnClickListener(v -> showRepayDialog());
    }

    private void fetchRepaymentDetails(int loanId) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<RepaymentResponse> call = apiService.getRepayments(loanId);  // Make sure this is Call<RepaymentResponse>

        call.enqueue(new Callback<RepaymentResponse>() {
            @Override
            public void onResponse(Call<RepaymentResponse> call, Response<RepaymentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RepaymentResponse repaymentDetails = response.body();

                    if (repaymentDetails.getRepayments() != null && !repaymentDetails.getRepayments().isEmpty()) {
                        Repayment latest = repaymentDetails.getRepayments().get(0);

                        companyName.setText(latest.getCompanyName());
                        amount.setText("Kshs. " + latest.getAmount());

                        List<Payment> paymentList = mapRepaymentsToPayments(repaymentDetails.getRepayments());
                        adapter.setData(paymentList);
                    } else {
                        Toast.makeText(RepaymentDetailsActivity.this, "No repayment data found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RepaymentDetailsActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RepaymentResponse> call, Throwable t) {
                Log.e("API_ERROR", t.getMessage());
                Toast.makeText(RepaymentDetailsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private List<Payment> mapRepaymentsToPayments(List<Repayment> repayments) {
        List<Payment> paymentList = new ArrayList<>();
        for (Repayment r : repayments) {
            // Create Payment object using Repayment data
            String ref = r.getLoanId(); // You can map loanId to ref
            String date = "Unknown"; // Set a placeholder for date or fetch actual date from Repayment if available
            String amount = r.getAmount(); // Map amount directly from Repayment

            paymentList.add(new Payment(ref, date, amount));
        }
        return paymentList;
    }

    private void showRepayDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_repay, null);
        EditText repayAmountInput = dialogView.findViewById(R.id.repay_amount_input);
        Button submitRepaymentButton = dialogView.findViewById(R.id.submitRepayment);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        submitRepaymentButton.setOnClickListener(v -> {
            String repayAmountStr = repayAmountInput.getText().toString().trim();
            if (repayAmountStr.isEmpty()) {
                Toast.makeText(this, "Please enter a repayment amount", Toast.LENGTH_SHORT).show();
                return;
            }

            double repayAmount;
            try {
                repayAmount = Double.parseDouble(repayAmountStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Enter a valid number", Toast.LENGTH_SHORT).show();
                return;
            }

            if (repayAmount <= 0) {
                Toast.makeText(this, "Amount must be greater than zero", Toast.LENGTH_SHORT).show();
                return;
            }

            dialog.dismiss(); // Hide dialog before calling repayment
            processRepayment(loanId, repayAmount); // Use correct loanId
        });

        dialog.show();
    }

    private void processRepayment(int loanId, double amount) {
        // Disable the repay button temporarily
        Button repayButton = findViewById(R.id.repayButton);
        repayButton.setEnabled(false);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing repayment...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Repayment request = new Repayment(String.valueOf(loanId), String.valueOf(amount), "Unknown Company", "REF-UNKNOWN");
        Call<GenericResponse> call = apiService.submitRepayment(request);
        call.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                progressDialog.dismiss();
                repayButton.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RepaymentDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    fetchRepaymentDetails(loanId); // Refresh repayment list
                } else {
                    Toast.makeText(RepaymentDetailsActivity.this, "Repayment failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                progressDialog.dismiss();
                repayButton.setEnabled(true);
                Toast.makeText(RepaymentDetailsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
