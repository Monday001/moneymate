package com.example.moneymate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymate.adapter.LenderHistoryAdapter;
import com.example.moneymate.databinding.FragmentBorrowerHomeBinding;
import com.example.moneymate.models.LenderHistory;
import com.example.moneymate.models.LoanApplication;
import com.example.moneymate.models.LoanResponse;
import com.example.moneymate.network.ApiClient;
import com.example.moneymate.network.ApiService;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BorrowerHomeFragment extends Fragment {

    private FragmentBorrowerHomeBinding binding;
    private RecyclerView recyclerView;
    private TextView nameTextView, loanBalanceTextView;
    private LenderHistoryAdapter adapter;
    private String username;
    private View userBorrowerView;  // Reference to the userBorrower view

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBorrowerHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameTextView = view.findViewById(R.id.borrower_name);
        loanBalanceTextView = view.findViewById(R.id.loan_balance);
        recyclerView = view.findViewById(R.id.borrower_history_recycler_view);

        // Find the userBorrower view by its ID
        userBorrowerView = view.findViewById(R.id.userBorrower);

        // Set the click listener for the userBorrower view to show the logout dialog
        userBorrowerView.setOnClickListener(v -> showLogoutDialog());

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "Default User");
        nameTextView.setText("Hi " + username);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);

        fetchLoanHistory();
    }

    private void fetchLoanHistory() {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        apiService.getLoanApplications().enqueue(new Callback<LoanResponse>() {
            @Override
            public void onResponse(Call<LoanResponse> call, Response<LoanResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<LoanApplication> loanApps = response.body().getApplications();
                    List<LenderHistory> historyList = new ArrayList<>();

                    double totalBalance = 0;

                    for (LoanApplication loanApp : loanApps) {
                        // Only include loans that are not repaid
                        if (!"repaid".equalsIgnoreCase(loanApp.getStatus())) {
                            totalBalance += Double.parseDouble(loanApp.getAmount());

                            historyList.add(new LenderHistory(
                                    username,
                                    String.valueOf(loanApp.getAmount()),
                                    loanApp.getStatus(),
                                    loanApp.getDateApplied(),
                                    "N/A",
                                    "N/A",
                                    "N/A",
                                    "N/A",
                                    loanApp.getPurpose(),
                                    null
                            ));
                        }
                    }

                    loanBalanceTextView.setText("Kshs. " + String.format("%,.2f", totalBalance));
                    adapter = new LenderHistoryAdapter(requireContext(), historyList);
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.e("LoanFetch", "Response unsuccessful or body null");
                }
            }

            @Override
            public void onFailure(Call<LoanResponse> call, Throwable t) {
                Log.e("LoanFetch", "API call failed", t);
            }
        });
    }

    // Method to show the logout confirmation dialog
    private void showLogoutDialog() {
        new android.app.AlertDialog.Builder(getContext())
                .setTitle("Log Out")
                .setMessage("Are you sure you want to log out?")
                .setCancelable(false)  // The dialog cannot be dismissed by tapping outside
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logoutUser(); // Perform logout if the user confirms
                    }
                })
                .setNegativeButton("No", null)  // Dismiss dialog if user taps "No"
                .show();
    }

    // Method to handle the logout process
    private void logoutUser() {
        // Clear shared preferences to log out the user
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();  // Clear all data from SharedPreferences


        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
