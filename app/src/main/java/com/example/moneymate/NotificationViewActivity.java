package com.example.moneymate;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NotificationViewActivity extends AppCompatActivity {

    TextView tvName, tvAmount, tvStatus, tvMessage;
    Button btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);

        tvName = findViewById(R.id.notification_name);
        tvAmount = findViewById(R.id.detail_amount);
        tvStatus = findViewById(R.id.detail_status);
        tvMessage = findViewById(R.id.messageTextView);
        btnClose = findViewById(R.id.btnClose);

        // Get data from intent
        String fullName = getIntent().getStringExtra("full_name");
        String amount = getIntent().getStringExtra("amount");
        String status = getIntent().getStringExtra("status");
        String message = getIntent().getStringExtra("message");

        tvName.setText(fullName != null ? fullName : "N/A");
        tvAmount.setText(amount != null ? "Kshs. " + amount : "Kshs. 0.00");
        tvStatus.setText(status != null ? status : "Status not available");
        tvMessage.setText(message != null ? message : "No message available");

        btnClose.setOnClickListener(v -> finish());
    }
}
