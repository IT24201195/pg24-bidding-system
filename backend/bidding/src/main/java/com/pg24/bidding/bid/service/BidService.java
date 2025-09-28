package com.pg24.bidding.bid.service;

import com.pg24.bidding.auction.model.Auction;
import com.pg24.bidding.auction.repository.AuctionRepository;
import com.pg24.bidding.bid.dto.BidResponse;
import com.pg24.bidding.bid.dto.PlaceBidRequest;
import com.pg24.bidding.bid.model.Bid;
import com.pg24.bidding.bid.repository.BidRepository;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class BidService {

    private final AuctionRepository auctionRepo;
    private final BidRepository bidRepo;
    private final SimpMessagingTemplate messaging;

    public BidService(AuctionRepository auctionRepo, BidRepository bidRepo, SimpMessagingTemplate messaging) {
        this.auctionRepo = auctionRepo;
        this.bidRepo = bidRepo;
        this.messaging = messaging;
    }

    @Transactional
    public BidResponse placeBid(Long auctionId, PlaceBidRequest req) {
        if (req == null || req.amount() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "AMOUNT_REQUIRED");
        }

        Auction a = auctionRepo.findById(auctionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "AUCTION_NOT_FOUND"));

        if (!a.isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "AUCTION_ENDED_OR_INACTIVE");
        }

        // Highest so far, or base price if no bids yet
        BigDecimal current = bidRepo.findTopByAuctionIdOrderByAmountDesc(auctionId)
                .map(Bid::getAmount)
                .orElse(a.getBasePrice());

        // Must be strictly higher than current
        if (req.amount().compareTo(current) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "BID_NOT_HIGHER_THAN_CURRENT");
        }

        // Save bid
        Bid bid = new Bid();
        bid.setAuction(a);
        bid.setAmount(req.amount());
        bid = bidRepo.save(bid);

        // Realtime broadcast (for Highest Bid Display)
        Map<String, Object> payload = Map.of(
                "amount", bid.getAmount(),
                "at", bid.getCreatedAt().toString()
        );

        messaging.convertAndSend("/topic/auctions/" + auctionId + "/highest", (Object) payload);


        return new BidResponse(bid.getId(), bid.getAmount(), bid.getCreatedAt());
    }
}
