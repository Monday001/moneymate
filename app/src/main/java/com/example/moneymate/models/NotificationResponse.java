package com.example.moneymate.models;

import java.util.List;

public class NotificationResponse {
    private boolean success;
    private List<Notification> notifications;

    public boolean isSuccess() {
        return success;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }
}