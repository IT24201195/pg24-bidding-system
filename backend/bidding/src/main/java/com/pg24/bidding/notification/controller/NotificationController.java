package com.pg24.bidding.notification.controller;

import com.pg24.bidding.notification.model.Notification;
import com.pg24.bidding.notification.repository.NotificationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationRepository repo;

    public NotificationController(NotificationRepository repo) { this.repo = repo; }

    @GetMapping("/{email}")
    public List<Notification> byEmail(@PathVariable String email) {
        return repo.findByUser_EmailOrderByCreatedAtDesc(email);
    }
}
