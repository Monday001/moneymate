package com.example.moneymate;

import android.os.Bundle;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BorrowerHomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrower_home);

        bottomNav = findViewById(R.id.borrower_bottom_navigation);

        // Set up navigation item selection listener
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_borrower_profile) {
                selectedFragment = new ProfileFragment();
            } else if (itemId == R.id.navigation_notifications) {
                selectedFragment = new NotificationsFragment();
            } else if (itemId == R.id.navigation_p2p) {
                selectedFragment = new P2PFragment();
            } else {
                selectedFragment = new BorrowerHomeFragment();
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();

            return true;
        });

        // Handle intent to navigate directly to a specific fragment
        String navigateTo = getIntent().getStringExtra("navigate_to");
        if ("p2p".equals(navigateTo)) {
            bottomNav.setSelectedItemId(R.id.navigation_p2p); // triggers fragment load
        } else {
            bottomNav.setSelectedItemId(R.id.navigation_borrower_home);
        }
    }
}
