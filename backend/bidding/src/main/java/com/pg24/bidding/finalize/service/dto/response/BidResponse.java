package com.example.Bidding.System.dto.response;

import java.time.LocalDateTime;

public class BidResponse {
    private Long id;
    private Long auctionId;
    private Long bidderId;
    private Double amount;
    private LocalDateTime bidTime;
    private Boolean isWinningBid;
    private String bidderName;

    // Constructors
    public BidResponse() {}

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

    public String getBidderName() { return bidderName; }
    public void setBidderName(String bidderName) { this.bidderName = bidderName; }
}