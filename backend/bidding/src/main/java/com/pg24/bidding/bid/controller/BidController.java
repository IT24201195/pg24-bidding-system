package com.pg24.bidding.bid.controller;

import com.pg24.bidding.bid.dto.BidResponse;
import com.pg24.bidding.bid.dto.PlaceBidRequest;
import com.pg24.bidding.bid.model.Bid;
import com.pg24.bidding.bid.repository.BidRepository;
import com.pg24.bidding.bid.service.BidService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/bids")
public class BidController {

    private final BidService service;
    private final BidRepository repo;

    public BidController(BidService service, BidRepository repo) {
        this.service = service; this.repo = repo;
    }

    @PostMapping("/auction/{auctionId}")
    public ResponseEntity<BidResponse> place(@PathVariable Long auctionId, @RequestBody PlaceBidRequest req) {
        Bid b = service.place(auctionId, req.bidderEmail(), req.amount());
        return ResponseEntity.ok(new BidResponse(
                b.getId(), b.getAuction().getId(), b.getBidder().getEmail(), b.getAmount(), b.getCreatedAt()
        ));
    }

    @GetMapping("/auction/{auctionId}")
    public List<Bid> list(@PathVariable Long auctionId) {
        return repo.findByAuction_IdOrderByAmountDesc(auctionId);
    }
}
