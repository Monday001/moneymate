package com.example.moneymate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymate.adapter.P2PAdapter;
import com.example.moneymate.models.P2PItem;
import com.example.moneymate.models.P2PResponse;
import com.example.moneymate.network.ApiClient;
import com.example.moneymate.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class P2PFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView emptyView;
    private List<P2PItem> companyList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_p2p, container, false);

        recyclerView = view.findViewById(R.id.p2p_recycler_view);
        progressBar = view.findViewById(R.id.p2p_progress_bar);
        emptyView = view.findViewById(R.id.p2p_empty_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);

        fetchAllLenders();

        return view;
    }

    private void fetchAllLenders() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        Call<P2PResponse> call = apiService.getLendersWithTerms();
        call.enqueue(new Callback<P2PResponse>() {
            @Override
            public void onResponse(Call<P2PResponse> call, Response<P2PResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    companyList = response.body().getCompanies();

                    if (companyList != null && !companyList.isEmpty()) {
                        recyclerView.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.GONE);

                        P2PAdapter adapter = new P2PAdapter(getContext(), companyList);
                        recyclerView.setAdapter(adapter);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                        emptyView.setText("No lenders found.");
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                    emptyView.setVisibility(View.VISIBLE);
                    emptyView.setText("Failed to load data.");
                }
            }

            @Override
            public void onFailure(Call<P2PResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setText("Connection error. Try again later.");
            }
        });
    }
}
