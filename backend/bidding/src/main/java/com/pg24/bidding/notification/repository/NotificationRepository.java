package com.pg24.bidding.notification.repository;

import com.pg24.bidding.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser_EmailOrderByCreatedAtDesc(String email);
}
