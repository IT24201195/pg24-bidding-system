package com.pg24.bidding.auction.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Auction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(length=1000) private String description;
    private BigDecimal basePrice;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    public boolean isActive() {
        var now = LocalDateTime.now();
        return (startAt == null || now.isAfter(startAt)) && (endAt == null || now.isBefore(endAt));
    }
    // getters/setters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public BigDecimal getBasePrice() { return basePrice; }
    public LocalDateTime getStartAt() { return startAt; }
    public LocalDateTime getEndAt() { return endAt; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }
    public void setStartAt(LocalDateTime startAt) { this.startAt = startAt; }
    public void setEndAt(LocalDateTime endAt) { this.endAt = endAt; }

}
