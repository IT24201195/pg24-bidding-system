package com.pg24.bidding.finalize.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "auction_results")
public class AuctionResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "auction_id", nullable = false)
    private Long auctionId;

    @Column(name = "winner_id")
    private Long winnerId;

    @Column(name = "winning_bid", precision = 19, scale = 2, nullable = false)
    private BigDecimal winningBid;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Column(name = "finalized_at")
    private LocalDateTime finalizedAt;

    @Column(name = "seller_notified")
    private Boolean sellerNotified = false;

    @Column(name = "winner_notified")
    private Boolean winnerNotified = false;

    @Column(name = "notification_count")
    private Integer notificationCount = 0;

    // Constructors
    public AuctionResult() {}

    public AuctionResult(Long auctionId, Long sellerId) {
        this.auctionId = auctionId;
        this.sellerId = sellerId;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAuctionId() { return auctionId; }
    public void setAuctionId(Long auctionId) { this.auctionId = auctionId; }

    public Long getWinnerId() { return winnerId; }
    public void setWinnerId(Long winnerId) { this.winnerId = winnerId; }

    public BigDecimal getWinningBid() { return winningBid; }
    public void setWinningBid(BigDecimal winningBid) { this.winningBid = winningBid; }

    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }

    public LocalDateTime getFinalizedAt() { return finalizedAt; }
    public void setFinalizedAt(LocalDateTime finalizedAt) { this.finalizedAt = finalizedAt; }

    public Boolean getSellerNotified() { return sellerNotified; }
    public void setSellerNotified(Boolean sellerNotified) { this.sellerNotified = sellerNotified; }

    public Boolean getWinnerNotified() { return winnerNotified; }
    public void setWinnerNotified(Boolean winnerNotified) { this.winnerNotified = winnerNotified; }

    public Integer getNotificationCount() { return notificationCount; }
    public void setNotificationCount(Integer notificationCount) { this.notificationCount = notificationCount; }

    @PrePersist
    protected void onCreate() {
        finalizedAt = LocalDateTime.now();
    }
}