package com.pg24.bidding.order.model;

import com.pg24.bidding.auction.model.Auction;
import com.pg24.bidding.auth.model.User;
import com.pg24.bidding.bid.model.Bid;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity @Table(name = "orders")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false) private Auction auction;
    @ManyToOne(optional = false) private User buyer;
    @OneToOne(optional = false) private Bid winningBid;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    private LocalDateTime createdAt;

    public enum PaymentStatus { PENDING, PAID, FAILED, CANCELLED }

    // getters/setters
    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public Auction getAuction(){return auction;}
    public void setAuction(Auction auction){this.auction=auction;}
    public User getBuyer(){return buyer;}
    public void setBuyer(User buyer){this.buyer=buyer;}
    public Bid getWinningBid(){return winningBid;}
    public void setWinningBid(Bid winningBid){this.winningBid=winningBid;}
    public PaymentStatus getPaymentStatus(){return paymentStatus;}
    public void setPaymentStatus(PaymentStatus paymentStatus){this.paymentStatus=paymentStatus;}
    public LocalDateTime getCreatedAt(){return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt){this.createdAt=createdAt;}
}
