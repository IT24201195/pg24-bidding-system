package com.pg24.bidding.auction.dto;

import com.pg24.bidding.auction.controller.AuctionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AuctionDTOs {
    public record AuctionResponse(
            Long id, String title, String description, BigDecimal basePrice, BigDecimal minIncrement,
            LocalDateTime startAt, LocalDateTime endAt, AuctionStatus status
    ) {}
}
