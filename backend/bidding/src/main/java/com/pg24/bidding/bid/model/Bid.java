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

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Auction auction;

    private Long bidderId;
    private BigDecimal amount;
    private LocalDateTime createdAt = LocalDateTime.now();

    // getters/setters
    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public Auction getAuction(){return auction;}
    public void setAuction(Auction auction){this.auction=auction;}
    public Long getBidderId(){return bidderId;}
    public void setBidderId(Long bidderId){this.bidderId=bidderId;}
    public BigDecimal getAmount(){return amount;}
    public void setAmount(BigDecimal amount){this.amount=amount;}
    public LocalDateTime getCreatedAt(){return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt){this.createdAt=createdAt;}
}
