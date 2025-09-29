package com.pg24.bidding.auction.repository;

import com.pg24.bidding.auction.controller.AuctionStatus;
import com.pg24.bidding.auction.model.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
    List<Auction> findByStatusAndEndAtBefore(AuctionStatus status, LocalDateTime time);
}
