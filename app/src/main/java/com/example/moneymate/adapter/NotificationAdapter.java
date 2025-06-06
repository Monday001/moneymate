package com.example.moneymate.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymate.NotificationViewActivity;
import com.example.moneymate.R;
import com.example.moneymate.models.Notification;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final List<Notification> notificationList;
    private final Context context;

    public NotificationAdapter(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFullname, tvAmount;
        Button viewButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFullname = itemView.findViewById(R.id.tvFullname);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            viewButton = itemView.findViewById(R.id.viewButton);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notificationList.get(position);

        final String lenderName;
        if (notification.getLender() != null && notification.getLender().getCompanyName() != null) {
            lenderName = notification.getLender().getCompanyName();
        } else {
            lenderName = "Unknown lender";
        }
        holder.tvFullname.setText(lenderName);

        final double amount = (notification.getLoan() != null) ? notification.getLoan().getAmount() : 0.0;
        holder.tvAmount.setText(String.format("Ksh %.2f", amount));

        final String status = notification.getStatus() != null ? notification.getStatus() : "N/A";
        final String message = notification.getMessage() != null ? notification.getMessage() : "No message available";

        holder.viewButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, NotificationViewActivity.class);
            intent.putExtra("full_name", lenderName);
            intent.putExtra("amount", String.format("%.2f", amount));
            intent.putExtra("status", status);
            intent.putExtra("message", message);
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return notificationList != null ? notificationList.size() : 0;
    }
}
