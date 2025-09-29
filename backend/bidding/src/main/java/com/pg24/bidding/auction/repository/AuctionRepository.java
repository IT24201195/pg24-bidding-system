package com.pg24.bidding.auction.repository;

import com.pg24.bidding.auction.model.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<Auction, Long> {}
