package com.example.moneymate;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymate.adapter.PaymentAdapter;
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

public class TransactionDetailActivity extends AppCompatActivity {

    private TextView nameText, amountText, statusText;
    private RecyclerView historyRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        nameText = findViewById(R.id.detail_name);
        amountText = findViewById(R.id.detail_amount);
        statusText = findViewById(R.id.detail_status);
        historyRecycler = findViewById(R.id.detail_payment_history);

        // Get data from Intent
        String name = getIntent().getStringExtra("name");
        String amount = getIntent().getStringExtra("amount");
        String status = getIntent().getStringExtra("status");

        // Set TextViews with the received data
        nameText.setText(name);
        amountText.setText("Kshs. " + amount);
        statusText.setText("Transaction " + status);

        // Set up RecyclerView and Adapter
        historyRecycler.setLayoutManager(new LinearLayoutManager(this));
        PaymentAdapter paymentAdapter = new PaymentAdapter();
        historyRecycler.setAdapter(paymentAdapter);

        // Get loan_id from Intent
        int loanId = getIntent().getIntExtra("loan_id", -1);

        if (loanId != -1) {
            // Fetch repayment details from API
            ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
            apiService.getRepayments(loanId).enqueue(new Callback<RepaymentResponse>() {
                @Override
                public void onResponse(Call<RepaymentResponse> call, Response<RepaymentResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        RepaymentResponse repaymentResponse = response.body();

                        // Check if repayments are available
                        if (repaymentResponse.getRepayments() != null && !repaymentResponse.getRepayments().isEmpty()) {
                            // Convert the list of Repayment objects to Payment objects
                            List<Payment> paymentList = mapRepaymentsToPayments(repaymentResponse.getRepayments());
                            // Set the list of repayments in the adapter
                            paymentAdapter.setPaymentList(paymentList);
                        } else {
                            // Handle empty repayment list
                        }
                    } else {
                        // Handle unsuccessful response
                    }
                }

                @Override
                public void onFailure(Call<RepaymentResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            // Handle missing loan_id error
        }

        // Close button functionality
        Button closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Closes the current activity
            }
        });
    }

    // Helper method to map Repayment to Payment
    private List<Payment> mapRepaymentsToPayments(List<Repayment> repayments) {
        List<Payment> paymentList = new ArrayList<>();
        for (Repayment repayment : repayments) {
            String referenceCode = repayment.getReferenceCode(); // For transactionId
            String date = repayment.getDate();                   // For paymentDate
            String amount = repayment.getAmount();               // For paymentAmount

            Payment payment = new Payment(referenceCode, date, amount);
            paymentList.add(payment);
        }
        return paymentList;
    }

}
