package com.example.Bidding.System.controller;

import com.example.Bidding.System.dto.requset.PlaceBidRequest;
import com.example.Bidding.System.dto.response.ApiResponse;
import com.example.Bidding.System.dto.response.BidResponse;
import com.example.Bidding.System.entity.Bid;
import com.example.Bidding.System.service.BidService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bids")
@CrossOrigin(origins = "*")
public class BidController {

    @Autowired
    private BidService bidService;

    @PostMapping
    public ResponseEntity<ApiResponse<Bid>> placeBid(@Valid @RequestBody PlaceBidRequest request) {
        try {
            Bid bid = bidService.placeBid(request);
            return ResponseEntity.ok(ApiResponse.success("Bid placed successfully", bid));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error placing bid: " + e.getMessage()));
        }
    }

    @GetMapping("/auction/{auctionId}")
    public ResponseEntity<ApiResponse<List<BidResponse>>> getBidsByAuction(@PathVariable Long auctionId) {
        try {
            List<BidResponse> bids = bidService.getBidsByAuction(auctionId);
            return ResponseEntity.ok(ApiResponse.success("Bids retrieved successfully", bids));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error retrieving bids: " + e.getMessage()));
        }
    }

    @GetMapping("/bidder/{bidderId}")
    public ResponseEntity<ApiResponse<List<BidResponse>>> getBidsByBidder(@PathVariable Long bidderId) {
        try {
            List<BidResponse> bids = bidService.getBidsByBidder(bidderId);
            return ResponseEntity.ok(ApiResponse.success("Bidder bids retrieved successfully", bids));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error retrieving bidder bids: " + e.getMessage()));
        }
    }

    @GetMapping("/auction/{auctionId}/highest")
    public ResponseEntity<ApiResponse<BidResponse>> getHighestBid(@PathVariable Long auctionId) {
        try {
            BidResponse highestBid = bidService.getHighestBid(auctionId);
            return ResponseEntity.ok(ApiResponse.success("Highest bid retrieved successfully", highestBid));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error retrieving highest bid: " + e.getMessage()));
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<String>> validateBid(@Valid @RequestBody PlaceBidRequest request) {
        try {
            bidService.validateBid(request);
            return ResponseEntity.ok(ApiResponse.success("Bid is valid"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Bid validation failed: " + e.getMessage()));
        }
    }
}