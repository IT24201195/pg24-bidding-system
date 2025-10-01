package com.example.Bidding.System.dto.requset;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PlaceBidRequest {
    @NotNull(message = "Auction ID is required")
    private Long auctionId;

    @NotNull(message = "Bidder ID is required")
    private Long bidderId;

    @NotNull(message = "Bid amount is required")
    @Positive(message = "Bid amount must be positive")
    private Double amount;

    // Constructors
    public PlaceBidRequest() {}

    public PlaceBidRequest(Long auctionId, Long bidderId, Double amount) {
        this.auctionId = auctionId;
        this.bidderId = bidderId;
        this.amount = amount;
    }

    // Getters and Setters
    public Long getAuctionId() { return auctionId; }
    public void setAuctionId(Long auctionId) { this.auctionId = auctionId; }

    public Long getBidderId() { return bidderId; }
    public void setBidderId(Long bidderId) { this.bidderId = bidderId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}