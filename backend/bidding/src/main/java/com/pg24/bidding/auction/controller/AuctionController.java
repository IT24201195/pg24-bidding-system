package com.pg24.bidding.auction.controller;

import com.pg24.bidding.auction.dto.CreateAuctionRequest;
import com.pg24.bidding.auction.model.Auction;
import com.pg24.bidding.auction.service.AuctionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/auctions")
public class AuctionController {
    private final AuctionService service;
    public AuctionController(AuctionService service){ this.service = service; }

    @PostMapping
    public ResponseEntity<Auction> create(@RequestBody CreateAuctionRequest req){
        return ResponseEntity.ok(service.create(req));
    }
}
