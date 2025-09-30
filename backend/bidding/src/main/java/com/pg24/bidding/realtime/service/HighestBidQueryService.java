package com.pg24.bidding.realtime.service;

import com.pg24.bidding.bid.repository.BidRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class HighestBidQueryService {
    private final BidRepository bids;

    public HighestBidQueryService(BidRepository bids) { this.bids = bids; }

    public BigDecimal currentHighest(Long auctionId) {
        return bids.findTopByAuction_IdOrderByAmountDesc(auctionId)
                .map(b -> b.getAmount())
                .orElse(BigDecimal.ZERO);
    }
}
