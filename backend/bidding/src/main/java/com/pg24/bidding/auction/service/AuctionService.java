package com.pg24.bidding.auction.service;

import com.pg24.bidding.auction.dto.CreateAuctionRequest;
import com.pg24.bidding.auction.model.Auction;
import com.pg24.bidding.auction.repository.AuctionRepository;
import org.springframework.stereotype.Service;

@Service
public class AuctionService {
    private final AuctionRepository repo;
    public AuctionService(AuctionRepository repo){ this.repo = repo; }

    public Auction create(CreateAuctionRequest req){
        var a = new Auction();
        a.setTitle(req.title());
        a.setDescription(req.description());
        a.setBasePrice(req.basePrice());
        a.setEndAt(req.endAt());
        return repo.save(a);
    }
}
