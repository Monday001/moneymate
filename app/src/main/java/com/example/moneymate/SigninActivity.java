package com.example.moneymate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SigninActivity extends AppCompatActivity {

    private EditText editUsername, editPassword;
    private Button buttonSignIn;
    private TextView textSignUp, forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin); // Make sure this XML file is named correctly

        // Linking UI components
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        textSignUp = findViewById(R.id.textSignUp);
        forgotPassword = findViewById(R.id.forgotPassword);

        // Sign In button logic
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editUsername.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SigninActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                } else {
                    // TODO: Replace this with actual sign-in logic / API
                    Toast.makeText(SigninActivity.this, "Signed in as " + username, Toast.LENGTH_SHORT).show();

                     Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
                     startActivity(intent);
                     finish();
                }
            }
        });

        // Navigate to Sign Up screen
        textSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        // Forgot Password placeholder
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SigninActivity.this, "Forgot password clicked", Toast.LENGTH_SHORT).show();
                // TODO: Launch password recovery screen

                Intent intent = new Intent(SigninActivity.this, PassResetActivity.class);
                startActivity(intent);
            }
        });
    }
}
