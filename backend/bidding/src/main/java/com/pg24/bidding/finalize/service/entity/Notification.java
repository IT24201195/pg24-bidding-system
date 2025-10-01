package com.example.Bidding.System.entity;

import com.example.Bidding.System.enums.NotificationType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(name = "is_read")
    private Boolean isRead = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "related_auction_id")
    private Long relatedAuctionId;

    // Constructors
    public Notification() {}

    public Notification(Long userId, String title, String message, NotificationType type, Long relatedAuctionId) {
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.relatedAuctionId = relatedAuctionId;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }

    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Long getRelatedAuctionId() { return relatedAuctionId; }
    public void setRelatedAuctionId(Long relatedAuctionId) { this.relatedAuctionId = relatedAuctionId; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}