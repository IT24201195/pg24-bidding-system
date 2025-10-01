package com.example.Bidding.System.service;

import com.example.Bidding.System.dto.NotificationDTO;
import java.util.List;

public interface NotificationService {
    void createNotification(Long userId, String title, String message, String type, Long relatedAuctionId);
    List<NotificationDTO> getUserNotifications(Long userId);
    List<NotificationDTO> getUnreadNotifications(Long userId);
    void markAsRead(Long notificationId);
    void markAllAsRead(Long userId);
    Long getUnreadCount(Long userId);
}