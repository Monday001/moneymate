package com.example.moneymate;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymate.adapter.LenderHistoryAdapter;
import com.example.moneymate.models.LenderHistory;

import java.util.ArrayList;

public class LenderHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lender_home);

        RecyclerView recyclerView = findViewById(R.id.lender_history_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

// Create a list
        ArrayList<LenderHistory> historyList = new ArrayList<>();

// Example: Add some dummy data first
        historyList.add(new LenderHistory("John Doe", "10,000"));
        historyList.add(new LenderHistory("Jane Smith", "8,500"));
        historyList.add(new LenderHistory("Michael Scott", "12,000"));

// Set the adapter
        LenderHistoryAdapter adapter = new LenderHistoryAdapter(historyList);
        recyclerView.setAdapter(adapter);
    }
}