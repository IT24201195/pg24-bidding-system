package com.pg24.bidding.auction.service;

import com.pg24.bidding.auction.controller.AuctionStatus;
import com.pg24.bidding.auction.dto.CreateAuctionRequest;
import com.pg24.bidding.auction.model.Auction;
import com.pg24.bidding.auction.repository.AuctionRepository;
import com.pg24.bidding.auth.service.AuthService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuctionService {

    private final AuctionRepository repo;
    private final AuthService auth;

    public AuctionService(AuctionRepository repo, AuthService auth) {
        this.repo = repo;
        this.auth = auth;
    }


    public Auction create(CreateAuctionRequest req) {
        var seller = auth.getByEmail(req.getSellerEmail());
        var a = new Auction();
        a.setSeller(seller);
        a.setTitle(req.getTitle());
        a.setDescription(req.getDescription());
        a.setBasePrice(req.getBasePrice());
        if (req.getMinIncrement() != null) a.setMinIncrement(req.getMinIncrement());
        a.setStartAt(req.getStartAt());
        a.setEndAt(req.getEndAt());
        a.setStatus(com.pg24.bidding.auction.controller.AuctionStatus.ACTIVE);
        return repo.save(a);
    }



    public List<Auction> list() { return repo.findAll(); }

    public Auction get(Long id) {
        return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Auction not found"));
    }

    public List<Auction> endedActiveAuctions() {
        return repo.findByStatusAndEndAtBefore(AuctionStatus.ACTIVE, LocalDateTime.now());
    }
}
