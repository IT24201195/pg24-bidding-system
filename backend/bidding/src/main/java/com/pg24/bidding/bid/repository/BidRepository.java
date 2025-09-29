package com.pg24.bidding.bid.repository;

import com.pg24.bidding.bid.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {
    @Query("""
     select b from Bid b
     where b.auction.id = :auctionId
     order by b.amount desc, b.createdAt asc
  """)
    Optional<Bid> findTopByAuctionIdOrderByAmountDesc(Long auctionId);

    @Query("""
     select coalesce(max(b.amount), :basePrice)
     from Bid b
     where b.auction.id = :auctionId
  """)
    BigDecimal findCurrentHighestAmount(Long auctionId, BigDecimal basePrice);
}
