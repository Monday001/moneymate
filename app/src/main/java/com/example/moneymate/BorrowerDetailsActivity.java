package com.example.moneymate;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymate.adapter.PaymentHistoryAdapter;
import com.example.moneymate.models.Payment;

import java.util.ArrayList;
import java.util.List;

public class BorrowerDetailsActivity extends AppCompatActivity {

    private TextView companyNameText, loanAmountText, statusText;
    private RecyclerView paymentHistoryRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrower_details);

        companyNameText = findViewById(R.id.detail_companyName);
        loanAmountText = findViewById(R.id.detail_loanAmount);
        statusText = findViewById(R.id.detail_loansStatus);
        paymentHistoryRecycler = findViewById(R.id.detail_loanPayment_history);

        // Receiving data from Intent
        String companyName = getIntent().getStringExtra("companyName");
        String loanAmount = getIntent().getStringExtra("loanAmount");
        String status = getIntent().getStringExtra("status");
        List<Payment> paymentHistory = (List<Payment>) getIntent().getSerializableExtra("paymentHistory");

        // Set data
        companyNameText.setText(companyName);
        loanAmountText.setText("Kshs. " + loanAmount);
        statusText.setText("Transaction " + status);

        // Setup RecyclerView
        paymentHistoryRecycler.setLayoutManager(new LinearLayoutManager(this));
        paymentHistoryRecycler.setAdapter(new PaymentHistoryAdapter(paymentHistory != null ? paymentHistory : new ArrayList<>()));

        // Close button
        Button closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> finish());
    }
}
