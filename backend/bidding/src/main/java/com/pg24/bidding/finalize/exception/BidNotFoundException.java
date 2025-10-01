package com.pg24.bidding.finalize.exception;

public class BidNotFoundException extends RuntimeException {
    public BidNotFoundException(String message) {
        super(message);
    }

    public BidNotFoundException(Long bidId) {
        super("Bid not found with id: " + bidId);
    }
}