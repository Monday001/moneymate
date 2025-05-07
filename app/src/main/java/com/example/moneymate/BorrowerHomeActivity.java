package com.example.moneymate;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BorrowerHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrower_home);

        // Load the BorrowerHomeFragment
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new BorrowerHomeFragment());
            transaction.commit();
        }

        BottomNavigationView bottomNav = findViewById(R.id.borrower_bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;

            int itemId = item.getItemId();

            if (itemId == R.id.navigation_borrower_profile) {
                selectedFragment = new ProfileFragment();
            }else if (itemId == R.id.navigation_notifications) {
                selectedFragment = new NotificationsFragment();
            }else if (itemId == R.id.navigation_p2p) {
                selectedFragment = new P2PFragment();}
            else if (itemId == R.id.navigation_borrower_home) {
                selectedFragment = new BorrowerHomeFragment();
            } else {
                selectedFragment = new BorrowerHomeFragment(); // default fallback
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();

            return true;
        });

    }

}