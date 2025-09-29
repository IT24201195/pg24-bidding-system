package com.pg24.bidding.bid.service;

import com.pg24.bidding.auction.model.Auction;
import com.pg24.bidding.auction.repository.AuctionRepository;
import com.pg24.bidding.bid.dto.BidDTOs.PlaceBidRequest;
import com.pg24.bidding.bid.model.Bid;
import com.pg24.bidding.bid.repository.BidRepository;
import com.pg24.bidding.realtime.RealtimePublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class BidService {
    private final BidRepository bidRepo;
    private final AuctionRepository auctionRepo;
    private final RealtimePublisher realtime;

    public BidService(BidRepository bidRepo, AuctionRepository auctionRepo, RealtimePublisher realtime) {
        this.bidRepo = bidRepo;
        this.auctionRepo = auctionRepo;
        this.realtime = realtime;
    }

    @Transactional
    public Bid place(Long auctionId, PlaceBidRequest req) {
        Auction auction = auctionRepo.findById(auctionId).orElseThrow(() -> new IllegalArgumentException("Auction not found"));
        if (auction.isEnded()) throw new IllegalStateException("Auction ended");

        BigDecimal current = bidRepo.findCurrentHighestAmount(auctionId, auction.getBasePrice());
        if (req.amount().compareTo(current) <= 0) {
            throw new IllegalArgumentException("Bid must be greater than " + current);
        }

        Bid bid = new Bid();
        bid.setAuction(auction);
        bid.setBidderId(req.bidderId());
        bid.setAmount(req.amount());
        bidRepo.save(bid);

        String masked = "User" + req.bidderId().toString().charAt(0) + "***" + (req.bidderId() % 10);
        realtime.publishHighestBid(auctionId, req.amount(), masked);
        return bid;
    }
}
