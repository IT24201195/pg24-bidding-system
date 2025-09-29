package com.pg24.bidding.bid.service;

import com.pg24.bidding.auction.controller.AuctionStatus;
import com.pg24.bidding.auction.model.Auction;
import com.pg24.bidding.auction.repository.AuctionRepository;
import com.pg24.bidding.auth.service.AuthService;
import com.pg24.bidding.bid.model.Bid;
import com.pg24.bidding.bid.repository.BidRepository;
import com.pg24.bidding.realtime.model.RealtimePublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class BidService {

    private final BidRepository repo;
    private final AuctionRepository auctions;
    private final AuthService auth;
    private final RealtimePublisher rt;

    public BidService(BidRepository repo, AuctionRepository auctions, AuthService auth, RealtimePublisher rt) {
        this.repo = repo;
        this.auctions = auctions;
        this.auth = auth;
        this.rt = rt;
    }

    @Transactional
    public Bid place(Long auctionId, String bidderEmail, BigDecimal amount) {
        Auction a = auctions.findById(auctionId)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Auction not found"));
        if (a.getStatus() != AuctionStatus.ACTIVE) throw new IllegalStateException("Auction not active");
        var now = LocalDateTime.now();
        if (a.getStartAt() != null && now.isBefore(a.getStartAt())) throw new IllegalStateException("Auction not started");
        if (a.getEndAt() != null && now.isAfter(a.getEndAt())) throw new IllegalStateException("Auction ended");

        BigDecimal min = a.getBasePrice() == null ? BigDecimal.ZERO : a.getBasePrice();
        var current = repo.findTopByAuction_IdOrderByAmountDesc(auctionId).orElse(null);
        var increment = a.getMinIncrement() == null ? BigDecimal.valueOf(100) : a.getMinIncrement();
        if (current != null) min = current.getAmount().add(increment);

        if (amount.compareTo(min) < 0) throw new IllegalStateException("Bid must be >= " + min);

        var bidder = auth.getByEmail(bidderEmail);
        Bid b = new Bid();
        b.setAuction(a); b.setBidder(bidder); b.setAmount(amount); b.setCreatedAt(LocalDateTime.now());
        repo.save(b);

        // realtime update for HighestBidWidget
        rt.publishHighestBid(auctionId, amount);
        return b;
    }
}
