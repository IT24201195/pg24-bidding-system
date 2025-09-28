package com.pg24.bidding.bid.model;

import com.pg24.bidding.auction.model.Auction;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(indexes = {
        @Index(name="idx_bid_auction", columnList = "auction_id"),
        @Index(name="idx_bid_amount_desc", columnList = "amount DESC")
})
public class Bid {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    private Auction auction;

    private Long bidderId;
    private BigDecimal amount;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }

    public Auction getAuction() { return auction; }
    public void setAuction(Auction auction) { this.auction = auction; }

    public java.math.BigDecimal getAmount() { return amount; }
    public void setAmount(java.math.BigDecimal amount) { this.amount = amount; }

    public java.time.LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; }
// getters/setters
}
