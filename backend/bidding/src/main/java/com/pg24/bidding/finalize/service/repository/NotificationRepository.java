package com.example.Bidding.System.repository;

import com.example.Bidding.System.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId);

    @Query("select count(n) from Notification n where n.userId = :userId and n.isRead = false")
    Long countUnreadByUserId(Long userId);
}


