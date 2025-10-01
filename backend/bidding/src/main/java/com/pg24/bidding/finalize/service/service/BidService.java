package com.example.Bidding.System.service;

import com.example.Bidding.System.dto.requset.PlaceBidRequest;
import com.example.Bidding.System.dto.response.BidResponse;
import com.example.Bidding.System.entity.Bid;
import java.util.List;

public interface BidService {
    Bid placeBid(PlaceBidRequest request);
    List<BidResponse> getBidsByAuction(Long auctionId);
    List<BidResponse> getBidsByBidder(Long bidderId);
    BidResponse getHighestBid(Long auctionId);
    void validateBid(PlaceBidRequest request);
}