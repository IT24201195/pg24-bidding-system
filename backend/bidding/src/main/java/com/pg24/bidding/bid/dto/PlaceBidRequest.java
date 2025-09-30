package com.pg24.bidding.bid.dto;

import java.math.BigDecimal;

public record PlaceBidRequest(String bidderEmail, BigDecimal amount) {}
