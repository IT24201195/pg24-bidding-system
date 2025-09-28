package com.pg24.bidding.bid.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BidResponse(Long bidId, BigDecimal amount, LocalDateTime createdAt) {}
