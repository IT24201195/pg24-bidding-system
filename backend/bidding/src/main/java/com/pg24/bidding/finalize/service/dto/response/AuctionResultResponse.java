package com.example.Bidding.System.dto.response;

import java.time.LocalDateTime;

public class AuctionResultResponse {
    private Long id;
    private Long auctionId;
    private String auctionTitle;
    private Long winnerId;
    private String winnerName;
    private Double winningBid;
    private Long sellerId;
    private String sellerName;
    private LocalDateTime finalizedAt;
    private Boolean sellerNotified;
    private Boolean winnerNotified;
    private Integer notificationCount;

    // Constructors
    public AuctionResultResponse() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAuctionId() { return auctionId; }
    public void setAuctionId(Long auctionId) { this.auctionId = auctionId; }

    public String getAuctionTitle() { return auctionTitle; }
    public void setAuctionTitle(String auctionTitle) { this.auctionTitle = auctionTitle; }

    public Long getWinnerId() { return winnerId; }
    public void setWinnerId(Long winnerId) { this.winnerId = winnerId; }

    public String getWinnerName() { return winnerName; }
    public void setWinnerName(String winnerName) { this.winnerName = winnerName; }

    public Double getWinningBid() { return winningBid; }
    public void setWinningBid(Double winningBid) { this.winningBid = winningBid; }

    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }

    public String getSellerName() { return sellerName; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }

    public LocalDateTime getFinalizedAt() { return finalizedAt; }
    public void setFinalizedAt(LocalDateTime finalizedAt) { this.finalizedAt = finalizedAt; }

    public Boolean getSellerNotified() { return sellerNotified; }
    public void setSellerNotified(Boolean sellerNotified) { this.sellerNotified = sellerNotified; }

    public Boolean getWinnerNotified() { return winnerNotified; }
    public void setWinnerNotified(Boolean winnerNotified) { this.winnerNotified = winnerNotified; }

    public Integer getNotificationCount() { return notificationCount; }
    public void setNotificationCount(Integer notificationCount) { this.notificationCount = notificationCount; }
}