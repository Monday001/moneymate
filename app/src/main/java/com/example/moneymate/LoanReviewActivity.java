package com.example.moneymate;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoanReviewActivity extends AppCompatActivity {

    TextView tvName, tvAmount, tvStatus, tvEmail, tvPhonenumber, tvIdFront, tvIdBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_review);

        // Link views
        tvName = findViewById(R.id.borrowerFullName);
        tvAmount = findViewById(R.id.loanAmount);
        tvStatus = findViewById(R.id.loanPurpose);  // You might want to rename this if it's for status
        tvPhonenumber = findViewById(R.id.borrowerPhone);
        tvEmail = findViewById(R.id.tvEmail); // Or whichever TextView you want to reuse
        tvIdFront = findViewById(R.id.tvIdFront);
        tvIdBack = findViewById(R.id.tvIdBack);

        // Get data from Intent
        String name = getIntent().getStringExtra("full_name");
        String amount = getIntent().getStringExtra("amount");
        String status = getIntent().getStringExtra("status");
        String phone = getIntent().getStringExtra("phone");
        String email = getIntent().getStringExtra("email");
        String idFront = getIntent().getStringExtra("id_front");
        String idBack = getIntent().getStringExtra("id_back");

        // Set values
        tvName.setText(name);
        tvAmount.setText("Kshs. " + amount);
        tvStatus.setText(status);
        tvPhonenumber.setText(phone);
        tvEmail.setText(email);
        tvIdFront.setText(idFront);
        tvIdBack.setText(idBack);

    }
}
