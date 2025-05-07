package com.example.moneymate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SuccessfulResetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_reset);

        Button continueButton = findViewById(R.id.verifyCodeButton);

        continueButton.setOnClickListener(v -> {
            Intent intent = new Intent(SuccessfulResetActivity.this, SigninActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
