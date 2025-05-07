package com.example.moneymate;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CompanyTermsActivity extends AppCompatActivity {
    TextView companyName, overview, terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_terms);

        companyName = findViewById(R.id.detailCompanyName);
        overview = findViewById(R.id.detailOverview);
        terms = findViewById(R.id.detailTerms);

        // Get data from Intent
        String name = getIntent().getStringExtra("company_name");
        String overviewText = getIntent().getStringExtra("overview");
        String termsText = getIntent().getStringExtra("terms");

        // Set data to views
        companyName.setText(name);
        overview.setText(overviewText);
        terms.setText(termsText);
    }
}
