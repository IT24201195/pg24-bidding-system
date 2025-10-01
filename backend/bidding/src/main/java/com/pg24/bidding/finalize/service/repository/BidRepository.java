package com.example.Bidding.System.repository;

import com.example.Bidding.System.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {

    List<Bid> findByAuctionIdOrderByAmountDesc(Long auctionId);

    Optional<Bid> findTopByAuctionIdOrderByAmountDesc(Long auctionId);

    @Query("SELECT b FROM Bid b WHERE b.auctionId = :auctionId ORDER BY b.amount DESC")
    List<Bid> findHighestBid(@Param("auctionId") Long auctionId);

    List<Bid> findByBidderId(Long bidderId);

    List<Bid> findByAuctionId(Long auctionId);

    @Query("SELECT COUNT(b) FROM Bid b WHERE b.auctionId = :auctionId")
    Long countBidsByAuctionId(@Param("auctionId") Long auctionId);

    @Query("SELECT MAX(b.amount) FROM Bid b WHERE b.auctionId = :auctionId")
    Optional<Double> findMaxBidAmountByAuctionId(@Param("auctionId") Long auctionId);
}