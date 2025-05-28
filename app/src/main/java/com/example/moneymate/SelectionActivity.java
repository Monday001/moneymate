package com.example.moneymate;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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
                Intent intent = new Intent(SelectionActivity.this, BorrowerHomeActivity.class); // Or BorrowerActivity if that's what you named it
                startActivity(intent);
            }
        });

        TextView title = findViewById(R.id.appTitle);
        String coloredTitle = "<font color=\"#3F00FF\">Money</font><font color=\"#F7931E\">Mate</font>";
        title.setText(Html.fromHtml(coloredTitle, Html.FROM_HTML_MODE_LEGACY));

    }
}
