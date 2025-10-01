package com.pg24.bidding.finalize.dto.request;

import jakarta.validation.constraints.NotNull;

public class FinalizeAuctionRequest {
    @NotNull(message = "Auction ID is required")
    private Long auctionId;

    // Constructors
    public FinalizeAuctionRequest() {}

    public FinalizeAuctionRequest(Long auctionId) {
        this.auctionId = auctionId;
    }

    // Getters and Setters
    public Long getAuctionId() { return auctionId; }
    public void setAuctionId(Long auctionId) { this.auctionId = auctionId; }
}