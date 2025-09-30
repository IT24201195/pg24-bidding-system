package com.pg24.bidding.notification.service;

import com.pg24.bidding.auth.model.User;
import com.pg24.bidding.notification.model.Notification;
import com.pg24.bidding.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    private final NotificationRepository notifications;

    public NotificationService(NotificationRepository notifications) {
        this.notifications = notifications;
    }

    public void notify(User user, String message) {
        Notification n = new Notification();
        n.setUser(user);
        n.setMessage(message);
        n.setCreatedAt(LocalDateTime.now());
        n.setReadFlag(false);
        notifications.save(n);
    }
}
