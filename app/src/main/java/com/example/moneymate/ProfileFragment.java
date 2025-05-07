package com.example.moneymate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView profileTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.borrower_profile_recycler_view);
        profileTitle = view.findViewById(R.id.profileTitle);

        // Set profile title or fetch user details here dynamically (like name)
        profileTitle.setText("Profile Details"); // Or dynamically set from shared preferences, API, etc.

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);

        fetchProfileDetails();
    }

    private void fetchProfileDetails() {
    }
}

