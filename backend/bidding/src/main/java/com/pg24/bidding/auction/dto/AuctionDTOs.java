package com.pg24.bidding.auction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AuctionDTOs {
    public record CreateAuctionRequest(String title, String description, BigDecimal basePrice, LocalDateTime endAt) {}
}
