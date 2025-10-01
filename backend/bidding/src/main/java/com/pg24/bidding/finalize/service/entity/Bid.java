package com.example.Bidding.System.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bids")
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "auction_id", nullable = false)
    private Long auctionId;

    @Column(name = "bidder_id", nullable = false)
    private Long bidderId;

    @Column(nullable = false)
    private Double amount;

    @Column(name = "bid_time")
    private LocalDateTime bidTime;

    @Column(name = "is_winning_bid")
    private Boolean isWinningBid = false;

    // Constructors
    public Bid() {}

    public Bid(Long auctionId, Long bidderId, Double amount) {
        this.auctionId = auctionId;
        this.bidderId = bidderId;
        this.amount = amount;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAuctionId() { return auctionId; }
    public void setAuctionId(Long auctionId) { this.auctionId = auctionId; }

    public Long getBidderId() { return bidderId; }
    public void setBidderId(Long bidderId) { this.bidderId = bidderId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public LocalDateTime getBidTime() { return bidTime; }
    public void setBidTime(LocalDateTime bidTime) { this.bidTime = bidTime; }

    public Boolean getIsWinningBid() { return isWinningBid; }
    public void setIsWinningBid(Boolean isWinningBid) { this.isWinningBid = isWinningBid; }

    @PrePersist
    protected void onCreate() {
        bidTime = LocalDateTime.now();
    }
}