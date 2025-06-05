package com.example.moneymate;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NotificationViewActivity extends AppCompatActivity {

    TextView notification_name, detail_amount, detail_status, messageTextView;
    Button btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);

        // Initialize views
        notification_name = findViewById(R.id.notification_name);
        detail_amount = findViewById(R.id.detail_amount);
        detail_status = findViewById(R.id.detail_status);
        messageTextView = findViewById(R.id.messageTextView);
        btnClose = findViewById(R.id.btnClose);

        // Get data from intent
        String fullName = getIntent().getStringExtra("full_name");
        String amount = getIntent().getStringExtra("amount");
        String status = getIntent().getStringExtra("status");
        String message = getIntent().getStringExtra("message");

        // Set values to views
        notification_name.setText(fullName);
        detail_amount.setText("Amount: Ksh " + amount);
        detail_status.setText("Status: " + status);
        messageTextView.setText(message);

        // Close button finishes the activity
        btnClose.setOnClickListener(v -> finish());
    }
}
