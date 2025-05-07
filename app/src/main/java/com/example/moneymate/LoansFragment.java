package com.example.moneymate;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymate.adapter.LoanHistoryAdapter;
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
import retrofit2.Retrofit;

public class LoansFragment extends Fragment {

    private RecyclerView recyclerView;
    private LoanHistoryAdapter adapter;

    public LoansFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lender_loans, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.lender_loans_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setNestedScrollingEnabled(false);

        fetchLoanApplications();
    }

    private void fetchLoanApplications() {
        Retrofit retrofit = ApiClient.getRetrofitInstance();
        ApiService apiService = retrofit.create(ApiService.class);

        apiService.getLoanApplications().enqueue(new Callback<LoanResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoanResponse> call, @NonNull Response<LoanResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<LoanApplication> loanApplications = response.body().getApplications();
                    List<LenderHistory> historyList = mapToLenderHistory(loanApplications);

                    adapter = new LoanHistoryAdapter(requireContext(), historyList, new LoanHistoryAdapter.OnItemClickListener() {
                        @Override
                        public void onViewClick(LenderHistory history) {
                            Intent intent = new Intent(requireContext(), LoanReviewActivity.class);
                            intent.putExtra("full_name", history.getName());
                            intent.putExtra("amount", history.getAmount());
                            intent.putExtra("purpose", history.getPurpose());
                            intent.putExtra("date", history.getDate());
                            intent.putExtra("phone", history.getPhone());
                            intent.putExtra("email", history.getEmail());
                            intent.putExtra("id_front", history.getIdFront());
                            intent.putExtra("id_back", history.getIdBack());
                            startActivity(intent);
                        }
                    });

                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(requireContext(), "Failed to load loan applications", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoanResponse> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<LenderHistory> mapToLenderHistory(List<LoanApplication> loanApplications) {
        List<LenderHistory> historyList = new ArrayList<>();
        for (LoanApplication app : loanApplications) {
            historyList.add(new LenderHistory(
                    app.getFullName(),
                    app.getAmount(),
                    app.getApplicationStatus(),
                    app.getDateSubmitted(),
                    app.getPhoneNumber(),
                    app.getEmailAddress(),
                    app.getIdFront(),
                    app.getIdBack(),
                    app.getPurpose(),
                    app.getPaymentHistory()
            ));
        }
        return historyList;
    }

}
