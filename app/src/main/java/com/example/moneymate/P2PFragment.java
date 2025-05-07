package com.example.moneymate;

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

import com.example.moneymate.adapter.P2PAdapter;
import com.example.moneymate.models.CompanyItem;
import com.example.moneymate.models.P2PResponse;
import com.example.moneymate.network.ApiClient;
import com.example.moneymate.network.ApiService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;

public class P2PFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<CompanyItem> companyList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_p2p, container, false); // Replace with your layout
        recyclerView = view.findViewById(R.id.p2p_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);

        // Call API to fetch P2P data
        fetchP2PData(1); // Pass the lender_id here, e.g., 1

        return view;
    }

    private void fetchP2PData(int lenderId) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        Map<String, Integer> body = new HashMap<>();
        body.put("lender_id", lenderId);

        Call<P2PResponse> call = apiService.getP2PData(body);
        call.enqueue(new Callback<P2PResponse>() {
            @Override
            public void onResponse(Call<P2PResponse> call, Response<P2PResponse> response) {
                if (response.isSuccessful()) {
                    // Log the raw response body
                    Log.d("P2PFragment", "Raw response: " + response.body().toString());

                    if (response.body() != null && response.body().isSuccess()) {
                        companyList = response.body().getCompanies(); // Get actual list of companies
                        P2PAdapter adapter = new P2PAdapter(getContext(), companyList, item -> {
                            Toast.makeText(getContext(), "Clicked: " + item.getName(), Toast.LENGTH_SHORT).show();
                        });
                        recyclerView.setAdapter(adapter);
                    } else {
                        Log.e("P2PFragment", "Failed to load data. Response unsuccessful or no data found.");
                        Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("P2PFragment", "API request failed: " + response.code());
                }
            }


            @Override
            public void onFailure(Call<P2PResponse> call, Throwable t) {
                // Log error
                Log.e("P2PFragment", "Error fetching P2P data: " + t.getMessage());
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
