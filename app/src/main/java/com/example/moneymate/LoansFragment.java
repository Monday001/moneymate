package com.example.moneymate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymate.adapter.LoanHistoryAdapter;
import com.example.moneymate.models.LenderHistory;
import com.example.moneymate.models.Loan;
import com.example.moneymate.models.LoanResponse;
import com.example.moneymate.network.ApiClient;
import com.example.moneymate.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoansFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView noLoansTextView;
    private LoanHistoryAdapter adapter;

    private static final String TAG = "LoansFragment";

    public LoansFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called");
        return inflater.inflate(R.layout.fragment_lender_loans, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated called");
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.lender_loans_recycler_view);
        noLoansTextView = view.findViewById(R.id.no_loans_text_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setNestedScrollingEnabled(false);

        fetchLoanApplications();
    }

    private void fetchLoanApplications() {
        Log.d(TAG, "fetchLoanApplications() called");

        SharedPreferences prefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String lenderIdStr = prefs.getString("lender_id", null);
        Log.d(TAG, "Retrieved lender_id from SharedPreferences = " + lenderIdStr);

        if (lenderIdStr == null) {
            Log.e(TAG, "lender_id not found in SharedPreferences");
            Toast.makeText(requireContext(), "Lender ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        int lenderId;
        try {
            lenderId = Integer.parseInt(lenderIdStr);
        } catch (NumberFormatException e) {
            Log.e(TAG, "Invalid lender_id format in SharedPreferences: " + lenderIdStr);
            Toast.makeText(requireContext(), "Invalid Lender ID format.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Creating Retrofit instance and making API call to getLoanApplications()");
        Retrofit retrofit = ApiClient.getRetrofitInstance();
        ApiService apiService = retrofit.create(ApiService.class);

        apiService.getLoanApplications(lenderId).enqueue(new Callback<LoanResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoanResponse> call, @NonNull Response<LoanResponse> response) {
                Log.d(TAG, "onResponse called. isSuccessful: " + response.isSuccessful());

                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "API response success: " + response.body().isSuccess());

                    if (response.body().isSuccess()) {
                        List<Loan> loanList = response.body().getApplications();
                        if (loanList == null) {
                            loanList = new ArrayList<>();
                        }

                        if (loanList.isEmpty()) {
                            noLoansTextView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            noLoansTextView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }

                        List<LenderHistory> historyList = convertToLenderHistoryList(loanList);

                        adapter = new LoanHistoryAdapter(requireContext(), historyList, history -> {
                            Log.d(TAG, "Loan clicked: ID=" + history.getLoanId() + ", Name=" + history.getName());
                            Intent intent = new Intent(requireContext(), LoanReviewActivity.class);
                            intent.putExtra("loan_id", history.getLoanId());
                            intent.putExtra("full_name", history.getName());
                            intent.putExtra("amount", history.getAmount());
                            intent.putExtra("purpose", history.getPurpose());
                            intent.putExtra("date", history.getDate());
                            intent.putExtra("phone", history.getPhone());
                            intent.putExtra("email", history.getEmail());
                            intent.putExtra("id_front", history.getIdFront());
                            intent.putExtra("id_back", history.getIdBack());
                            startActivity(intent);
                        });

                        recyclerView.setAdapter(adapter);
                    } else {
                        Log.e(TAG, "Response body marked failure. Message: " + response.body());
                        Toast.makeText(requireContext(), "Unable to load loan applications. Try again later.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "API response failed or null body");
                    Toast.makeText(requireContext(), "Unable to load loan applications. Try again later.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoanResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Network call failed: " + t.getMessage(), t);
                Toast.makeText(requireContext(), "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private List<LenderHistory> convertToLenderHistoryList(List<Loan> loans) {
        Log.d(TAG, "Converting Loan list to LenderHistory list");
        List<LenderHistory> historyList = new ArrayList<>();
        if (loans != null) {
            for (Loan loan : loans) {
                Log.d(TAG, "Processing loan for: " + loan.getFull_name());
                historyList.add(new LenderHistory(
                        loan.getId(),
                        loan.getFull_name(),
                        loan.getAmount(),
                        loan.getApplication_status(),
                        loan.getDate_submitted(),
                        loan.getPhone_number(),
                        loan.getEmail_address(),
                        loan.getId_front(),
                        loan.getId_back(),
                        loan.getPurpose(),
                        loan.getPaymentHistory()
                ));
            }
        } else {
            Log.d(TAG, "Loan list is null");
        }
        return historyList;
    }
}
