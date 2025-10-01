package com.example.Bidding.System.controller;

import com.example.Bidding.System.dto.requset.CreateAuctionRequest;
import com.example.Bidding.System.dto.response.ApiResponse;
import com.example.Bidding.System.dto.response.AuctionResponse;
import com.example.Bidding.System.entity.Auction;
import com.example.Bidding.System.service.AuctionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auctions")
@CrossOrigin(origins = "*")
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    @PostMapping
    public ResponseEntity<ApiResponse<Auction>> createAuction(@Valid @RequestBody CreateAuctionRequest request) {
        try {
            Auction auction = auctionService.createAuction(request);
            return ResponseEntity.ok(ApiResponse.success("Auction created successfully", auction));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error creating auction: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AuctionResponse>>> getAllAuctions() {
        try {
            List<AuctionResponse> auctions = auctionService.getAllAuctions();
            return ResponseEntity.ok(ApiResponse.success("Auctions retrieved successfully", auctions));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error retrieving auctions: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AuctionResponse>> getAuctionById(@PathVariable Long id) {
        try {
            AuctionResponse auction = auctionService.getAuctionById(id);
            return ResponseEntity.ok(ApiResponse.success("Auction retrieved successfully", auction));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error retrieving auction: " + e.getMessage()));
        }
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<ApiResponse<List<AuctionResponse>>> getAuctionsBySeller(@PathVariable Long sellerId) {
        try {
            List<AuctionResponse> auctions = auctionService.getAuctionsBySeller(sellerId);
            return ResponseEntity.ok(ApiResponse.success("Seller auctions retrieved successfully", auctions));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error retrieving seller auctions: " + e.getMessage()));
        }
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<AuctionResponse>>> getActiveAuctions() {
        try {
            List<AuctionResponse> auctions = auctionService.getActiveAuctions();
            return ResponseEntity.ok(ApiResponse.success("Active auctions retrieved successfully", auctions));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error retrieving active auctions: " + e.getMessage()));
        }
    }
}