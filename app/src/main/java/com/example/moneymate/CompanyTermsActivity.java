package com.example.moneymate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CompanyTermsActivity extends AppCompatActivity {
    TextView companyName, overview, terms;
    CheckBox termsCheckBox;
    Button applyButton, closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_terms);

        companyName = findViewById(R.id.detailCompanyName);
        overview = findViewById(R.id.detailOverview);
        terms = findViewById(R.id.detailTerms);
        termsCheckBox = findViewById(R.id.termsCheckBox);
        applyButton = findViewById(R.id.applyButton);
        closeButton = findViewById(R.id.closeButton);

        // Receive lender data from intent
        String name = getIntent().getStringExtra("company_name");
        String overviewText = getIntent().getStringExtra("overview");
        String termsText = getIntent().getStringExtra("terms");
        int lenderId = getIntent().getIntExtra("lender_id", -1);
        Log.d("CompanyTermsActivity", "Received lenderId: " + lenderId);

        companyName.setText(name);
        overview.setText(overviewText);
        terms.setText(termsText);

        // Enable apply button only when terms checkbox is checked
        termsCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            applyButton.setEnabled(isChecked);
        });

        closeButton.setOnClickListener(v -> finish());

        applyButton.setOnClickListener(v -> {
            if (lenderId == -1) {
                Toast.makeText(this, "Lender ID missing", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get borrower_id from SharedPreferences
            SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String borrowerId = preferences.getString("borrower_id", null);

            if (borrowerId == null) {
                Toast.makeText(this, "Borrower ID not found. Please sign in again.", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(CompanyTermsActivity.this, LoanApplicationActivity.class);
            intent.putExtra("lender_id", lenderId);
            intent.putExtra("borrower_id", borrowerId);
            startActivity(intent);
        });

    }
}
