package com.pg24.bidding.bid.model;

import com.pg24.bidding.auction.model.Auction;
import com.pg24.bidding.auth.model.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(indexes = {@Index(columnList = "auction_id"), @Index(columnList = "bidder_id")})
public class Bid {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) private Auction auction;
    @ManyToOne(optional = false) private User bidder;

    private BigDecimal amount;
    private LocalDateTime createdAt;

    // getters/setters
    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public Auction getAuction(){return auction;}
    public void setAuction(Auction auction){this.auction=auction;}
    public User getBidder(){return bidder;}
    public void setBidder(User bidder){this.bidder=bidder;}
    public BigDecimal getAmount(){return amount;}
    public void setAmount(BigDecimal amount){this.amount=amount;}
    public LocalDateTime getCreatedAt(){return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt){this.createdAt=createdAt;}
}
