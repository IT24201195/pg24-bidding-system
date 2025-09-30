package com.pg24.bidding.bid.repository;

import com.pg24.bidding.bid.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {
    Optional<Bid> findTopByAuction_IdOrderByAmountDesc(Long auctionId);
    List<Bid> findByAuction_IdOrderByAmountDesc(Long auctionId);
    List<Bid> findByBidder_IdOrderByCreatedAtDesc(Long bidderId);
}
