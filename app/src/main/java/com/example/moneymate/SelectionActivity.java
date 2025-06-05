package com.example.moneymate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_selection);

        Button lenderProceedBtn = findViewById(R.id.lender_proceed_button);
        lenderProceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectionActivity.this, AuthenticateLenderActivity.class);
                startActivity(intent);
            }
        });

        Button borrowerProceedBtn = findViewById(R.id.borrower_proceed_button);
        borrowerProceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String username = prefs.getString("username", "User");
                String userType = prefs.getString("user_type", "none");

                int borrowerId = -1;
                try {
                    String borrowerIdString = prefs.getString("borrower_id", "-1");
                    borrowerId = Integer.parseInt(borrowerIdString);
                } catch (NumberFormatException e) {
                    Log.e("SelectionActivity", "Invalid borrower_id format", e);
                }

                // Log values for debugging
                Log.d("SelectionActivity", "username: " + username);
                Log.d("SelectionActivity", "borrowerId: " + borrowerId);
                Log.d("SelectionActivity", "userType: " + userType);

                // Start BorrowerHomeActivity and pass username as fallback
                Intent intent = new Intent(SelectionActivity.this, BorrowerHomeActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        TextView title = findViewById(R.id.appTitle);
        String coloredTitle = "<font color=\"#3F00FF\">Money</font><font color=\"#F7931E\">Mate</font>";
        title.setText(Html.fromHtml(coloredTitle, Html.FROM_HTML_MODE_LEGACY));
    }
}
