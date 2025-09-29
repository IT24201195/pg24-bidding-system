package com.pg24.bidding.auction.service;

import com.pg24.bidding.auction.dto.AuctionDTOs.CreateAuctionRequest;
import com.pg24.bidding.auction.model.Auction;
import com.pg24.bidding.auction.repository.AuctionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuctionService {
    private final AuctionRepository repo;
    public AuctionService(AuctionRepository repo){this.repo=repo;}

    @Transactional
    public Auction create(CreateAuctionRequest req){
        var a = new Auction();
        a.setTitle(req.title());
        a.setDescription(req.description());
        a.setBasePrice(req.basePrice());
        a.setEndAt(req.endAt());
        return repo.save(a);
    }

    @Transactional(readOnly = true)
    public List<Auction> list(){ return repo.findAll(); }

    @Transactional(readOnly = true)
    public Auction get(Long id){ return repo.findById(id).orElseThrow(); }
}
