package com.example.Bidding.System.service;

import com.example.Bidding.System.dto.requset.CreateAuctionRequest;
import com.example.Bidding.System.dto.response.AuctionResponse;
import com.example.Bidding.System.dto.response.AuctionResultResponse;
import com.example.Bidding.System.entity.Auction;
import com.example.Bidding.System.entity.AuctionResult;
import java.util.List;

public interface AuctionService {
    Auction createAuction(CreateAuctionRequest request);
    List<AuctionResponse> getAllAuctions();
    AuctionResponse getAuctionById(Long id);
    List<AuctionResponse> getAuctionsBySeller(Long sellerId);
    List<AuctionResponse> getActiveAuctions();
    void finalizeExpiredAuctions();
    AuctionResult finalizeAuction(Long auctionId);
    AuctionResultResponse getAuctionResult(Long auctionId);
    List<AuctionResultResponse> getSellerClosedAuctions(Long sellerId);
    List<AuctionResultResponse> getBuyerWonAuctions(Long buyerId);
    Auction updateAuctionStatus(Long auctionId, String status);
}