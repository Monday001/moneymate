package com.example.moneymate;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button getStartedButton = findViewById(R.id.btnGetStarted);
        getStartedButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignupActivity.class); // Replace with your actual target activity
            startActivity(intent);
        });

        TextView title = findViewById(R.id.appTitle);
        String coloredTitle = "<font color=\"#3F00FF\">Money</font><font color=\"#F7931E\">Mate</font>";
        title.setText(Html.fromHtml(coloredTitle, Html.FROM_HTML_MODE_LEGACY));

    }
}
