package com.pg24.bidding.bid.dto;

import java.math.BigDecimal;

public class BidDTOs {
    public record PlaceBidRequest(Long bidderId, BigDecimal amount) {}
}
