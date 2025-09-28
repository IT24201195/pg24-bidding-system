package com.pg24.bidding.bid.controller;

import com.pg24.bidding.bid.dto.BidResponse;
import com.pg24.bidding.bid.dto.PlaceBidRequest;
import com.pg24.bidding.bid.repository.BidRepository;
import com.pg24.bidding.bid.service.BidService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auctions") // base path here
public class BidController {

    private final BidService service;
    private final BidRepository bidRepo;

    public BidController(BidService service, BidRepository bidRepo) {
        this.service = service;
        this.bidRepo = bidRepo;
    }

    // POST /api/auctions/{auctionId}/bids
    @PostMapping("/{auctionId}/bids")
    public ResponseEntity<BidResponse> place(
            @PathVariable Long auctionId,
            @RequestBody PlaceBidRequest req) {
        var res = service.placeBid(auctionId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // GET /api/auctions/{auctionId}/bids
    @GetMapping("/{auctionId}/bids")
    public List<BidResponse> list(@PathVariable Long auctionId) {
        return bidRepo.findTop50ByAuctionIdOrderByAmountDesc(auctionId)
                .stream()
                .map(b -> new BidResponse(b.getId(), b.getAmount(), b.getCreatedAt()))
                .toList();
    }
}
