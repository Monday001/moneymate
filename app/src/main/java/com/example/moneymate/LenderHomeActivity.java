package com.example.moneymate;

import com.example.moneymate.R;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LenderHomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lender_home);

        // Load the LenderHomeFragment
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new LenderHomeFragment());
            transaction.commit();
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;

            int itemId = item.getItemId();

            if (itemId == R.id.navigation_profile) {
                selectedFragment = new TermsFragment();
            }else if (itemId == R.id.navigation_loans) {
                selectedFragment = new LoansFragment();}
            else if (itemId == R.id.navigation_home) {
                selectedFragment = new LenderHomeFragment();
            } else {
                selectedFragment = new LenderHomeFragment(); // default fallback
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();

            return true;
        });

    }
}
