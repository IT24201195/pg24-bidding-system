package com.example.Bidding.System.service;

public interface EmailService {
    void sendWinnerNotification(Long winnerId, String auctionTitle, Double winningBid);
    void sendSellerNotification(Long sellerId, String auctionTitle, Double winningBid, Long winnerId);
    void sendNoWinnerNotification(Long sellerId, String auctionTitle);
    void sendOutbidNotification(Long bidderId, String auctionTitle, Double newBid);
}