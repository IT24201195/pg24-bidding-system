

package com.pg24.bidding.auction.controller;


public enum AuctionStatus {
    DRAFT,   // created but not yet published
    ACTIVE,  // visible and open for bidding
    FINISHED,// reached end time
    CANCELED // manually cancelled before any bid
}
