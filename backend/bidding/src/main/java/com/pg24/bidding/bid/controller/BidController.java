package com.pg24.bidding.bid.controller;

import com.pg24.bidding.bid.dto.BidDTOs.PlaceBidRequest;
import com.pg24.bidding.bid.model.Bid;
import com.pg24.bidding.bid.repository.BidRepository;
import com.pg24.bidding.bid.service.BidService;
import com.pg24.bidding.realtime.HighestBidDTO;
import com.pg24.bidding.realtime.HighestBidQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auctions/{auctionId}")
public class BidController {
    private final BidService service;
    private final HighestBidQueryService query;
    private final BidRepository repo;

    public BidController(BidService service, HighestBidQueryService query, BidRepository repo) {
        this.service = service;
        this.query = query;
        this.repo = repo;
    }

    @PostMapping("/bids")
    public ResponseEntity<Bid> place(@PathVariable Long auctionId, @RequestBody PlaceBidRequest req) {
        return ResponseEntity.ok(service.place(auctionId, req));
    }

    @GetMapping("/highest")
    public ResponseEntity<HighestBidDTO> highest(@PathVariable Long auctionId){
        return ResponseEntity.ok(query.currentHighest(auctionId));
    }
}
