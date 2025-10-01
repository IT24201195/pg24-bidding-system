package com.example.Bidding.System.exception;

public class AuctionNotFoundException extends RuntimeException {
    public AuctionNotFoundException(String message) {
        super(message);
    }

    public AuctionNotFoundException(Long auctionId) {
        super("Auction not found with id: " + auctionId);
    }
}