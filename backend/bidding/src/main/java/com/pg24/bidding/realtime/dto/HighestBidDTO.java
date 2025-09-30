package com.pg24.bidding.realtime.dto;

import java.math.BigDecimal;

public record HighestBidDTO(Long auctionId, BigDecimal amount) {}
