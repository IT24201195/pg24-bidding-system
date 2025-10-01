package com.pg24.bidding.finalize.controller;

import com.pg24.bidding.auth.dto.ApiResponse;
import com.pg24.bidding.auction.service.AuctionService;
import com.pg24.bidding.auction.model.Auction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auctions/finalization")
@CrossOrigin(origins = "*")
public class AuctionFinalizationController {

    @Autowired
    private AuctionService auctionService;

    @PostMapping("/finalize-expired")
    public ResponseEntity<ApiResponse<String>> finalizeExpiredAuctions() {
        try {
            auctionService.finalizeExpiredAuctions();
            return ResponseEntity.ok(ApiResponse.success("Expired auctions finalized successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error finalizing auctions: " + e.getMessage()));
        }
    }

    @PostMapping("/finalize/{auctionId}")
    public ResponseEntity<ApiResponse<Auction>> finalizeAuction(@PathVariable Long auctionId) {
        try {
            Auction result = auctionService.finalizeAuction(auctionId);
            return ResponseEntity.ok(ApiResponse.success("Auction finalized successfully", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error finalizing auction: " + e.getMessage()));
        }
    }

    @GetMapping("/results/{auctionId}")
    public ResponseEntity<ApiResponse<Auction>> getAuctionResult(@PathVariable Long auctionId) {
        try {
            Auction result = auctionService.getAuctionResult(auctionId);
            return ResponseEntity.ok(ApiResponse.success("Auction result retrieved successfully", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error retrieving auction result: " + e.getMessage()));
        }
    }

    @GetMapping("/seller/{sellerId}/closed")
    public ResponseEntity<ApiResponse<List<Auction>>> getSellerClosedAuctions(@PathVariable Long sellerId) {
        try {
            List<Auction> results = auctionService.getSellerClosedAuctions(sellerId);
            return ResponseEntity.ok(ApiResponse.success("Seller closed auctions retrieved successfully", results));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error retrieving seller closed auctions: " + e.getMessage()));
        }
    }

    @GetMapping("/buyer/{buyerId}/won")
    public ResponseEntity<ApiResponse<List<Auction>>> getBuyerWonAuctions(@PathVariable Long buyerId) {
        try {
            List<Auction> results = auctionService.getBuyerWonAuctions(buyerId);
            return ResponseEntity.ok(ApiResponse.success("Buyer won auctions retrieved successfully", results));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error retrieving buyer won auctions: " + e.getMessage()));
        }
    }

    @PutMapping("/{auctionId}/status/{status}")
    public ResponseEntity<ApiResponse<Object>> updateAuctionStatus(
            @PathVariable Long auctionId,
            @PathVariable String status) {
        try {
            auctionService.updateAuctionStatus(auctionId, status);
            return ResponseEntity.ok(ApiResponse.success("Auction status updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error updating auction status: " + e.getMessage()));
        }
    }
}