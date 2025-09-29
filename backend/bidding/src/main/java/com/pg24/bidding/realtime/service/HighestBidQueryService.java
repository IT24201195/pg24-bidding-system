package com.pg24.bidding.realtime.service;

import com.pg24.bidding.auction.model.Auction;
import com.pg24.bidding.auction.repository.AuctionRepository;
import com.pg24.bidding.bid.model.Bid;
import com.pg24.bidding.bid.repository.BidRepository;
import com.pg24.bidding.realtime.dto.HighestBidDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class HighestBidQueryService {
    private final BidRepository bidRepo;
    private final AuctionRepository auctionRepo;

    public HighestBidQueryService(BidRepository bidRepo, AuctionRepository auctionRepo) {
        this.bidRepo = bidRepo;
        this.auctionRepo = auctionRepo;
    }

    @Transactional(readOnly = true)
    public HighestBidDTO currentHighest(Long auctionId) {
        Auction auction = auctionRepo.findById(auctionId)
                .orElseThrow(() -> new IllegalArgumentException("Auction not found"));

        var top = bidRepo.findTopByAuctionIdOrderByAmountDesc(auctionId);
        BigDecimal amount = top.map(Bid::getAmount).orElse(auction.getBasePrice());
        String masked = top.map(b -> mask(b.getBidderId())).orElse("-");
        return new HighestBidDTO(auctionId, amount, masked);
    }

    private String mask(Long bidderId){
        if (bidderId == null) return "-";
        String s = bidderId.toString();
        return "User" + s.charAt(0) + "***" + (bidderId % 10);
    }
}
