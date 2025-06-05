package com.example.moneymate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymate.adapter.NotificationAdapter;
import com.example.moneymate.models.Notification;
import com.example.moneymate.network.ApiClient;
import com.example.moneymate.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {

    RecyclerView recyclerView;
    NotificationAdapter adapter;
    List<Notification> notifications = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        recyclerView = view.findViewById(R.id.notification_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get borrower ID from shared preferences
        SharedPreferences prefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String borrowerIdStr = prefs.getString("borrower_id", null);

        if (borrowerIdStr == null) {
            Toast.makeText(getContext(), "Borrower ID not found", Toast.LENGTH_SHORT).show();
            return view;
        }

        int borrowerId;
        try {
            borrowerId = Integer.parseInt(borrowerIdStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid borrower ID", Toast.LENGTH_SHORT).show();
            return view;
        }

        // Fetch notifications from API
        ApiService api = ApiClient.getRetrofitInstance().create(ApiService.class);
        api.getNotifications(borrowerId).enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    notifications = response.body();
                    adapter = new NotificationAdapter(getContext(), notifications);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "No notifications found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to fetch notifications", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
