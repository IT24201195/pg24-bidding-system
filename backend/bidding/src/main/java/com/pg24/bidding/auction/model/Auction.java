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
}
