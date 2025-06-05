package com.example.moneymate;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymate.adapter.LenderHistoryAdapter;
import com.example.moneymate.models.LenderHistory;
import com.example.moneymate.models.LenderHistoryResponse;
import com.example.moneymate.models.LoanStatusResponse;
import com.example.moneymate.network.ApiClient;
import com.example.moneymate.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LenderHomeFragment extends Fragment {

    private RecyclerView recyclerView;

    private LenderHistoryAdapter lenderHistoryAdapter;

    private TextView userTotal, approvedTotal, deniedTotal, pendingTotal;
    private View userLenderView;  // This will reference logout trigger View

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lender_home, container, false);

        // RecyclerView for history
        recyclerView = rootView.findViewById(R.id.lender_history_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        lenderHistoryAdapter = new LenderHistoryAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(lenderHistoryAdapter);

        // Status count TextViews
        userTotal = rootView.findViewById(R.id.userTotal);
        approvedTotal = rootView.findViewById(R.id.approvedTotal);
        deniedTotal = rootView.findViewById(R.id.deniedTotal);
        pendingTotal = rootView.findViewById(R.id.pendingTotal);

        // Find the 'userLender' view by ID
        userLenderView = rootView.findViewById(R.id.userLender);

        // Set up the click listener for the 'userLender' view
        userLenderView.setOnClickListener(v -> showLogoutDialog());

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get lenderId from SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences("UserPrefs", getContext().MODE_PRIVATE);
        String lenderIdStr = prefs.getString("lender_id", null);

        if (lenderIdStr != null) {
            try {
                int lenderId = Integer.parseInt(lenderIdStr);

                // Fetch status counts
                fetchStatusCounts(lenderId);

                // Fetch history
                fetchLenderHistory(lenderId);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid lender ID format", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Lender ID not found", Toast.LENGTH_SHORT).show();
        }
    }


    private void fetchStatusCounts(int lenderId) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        apiService.getLoanStatus(lenderId).enqueue(new Callback<LoanStatusResponse>() {
            @Override
            public void onResponse(Call<LoanStatusResponse> call, Response<LoanStatusResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    LoanStatusResponse data = response.body();
                    userTotal.setText(String.valueOf(data.getUser_count()));
                    approvedTotal.setText(String.valueOf(data.getLoan_status().getApproved_count()));
                    deniedTotal.setText(String.valueOf(data.getLoan_status().getDenied_count()));
                    pendingTotal.setText(String.valueOf(data.getLoan_status().getPending_count()));
                } else {
                    Toast.makeText(getContext(), "Failed to fetch status counts", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoanStatusResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchLenderHistory(int lenderId) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        apiService.getLenderHistory(lenderId).enqueue(new Callback<LenderHistoryResponse>() {
            @Override
            public void onResponse(Call<LenderHistoryResponse> call, Response<LenderHistoryResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<LenderHistory> historyList = response.body().getLoans();
                    lenderHistoryAdapter.setHistoryList(historyList);  // Update the adapter's list
                } else {
                    Toast.makeText(getContext(), "Failed to load lender history", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LenderHistoryResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    // Method to show logout confirmation dialog
    private void showLogoutDialog() {
        new android.app.AlertDialog.Builder(getContext())
                .setTitle("Log Out")
                .setMessage("Are you sure you want to log out?")
                .setCancelable(false)  // The dialog cannot be dismissed by tapping outside
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logoutUser(); // Perform logout if user confirms
                    }
                })
                .setNegativeButton("No", null)  // Dismiss dialog if user taps "No"
                .show();
    }

    // Method to handle the logout process
    private void logoutUser() {
        // Clear shared preferences to log out the user
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userPrefs", getContext().MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();  // Clear all data from SharedPreferences

        // Navigate to the login screen (AuthenticateLenderActivity)
        Intent intent = new Intent(getActivity(), AuthenticateLenderActivity.class);
        startActivity(intent);
        getActivity().finish();  // Close the current activity (LenderHomeFragment) so the user cannot navigate back to it
    }
}
