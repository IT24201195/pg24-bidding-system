package com.example.Bidding.System.repository;

import com.example.Bidding.System.entity.Auction;
import com.example.Bidding.System.enums.AuctionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {

    List<Auction> findByStatusAndEndTimeBefore(AuctionStatus status, LocalDateTime endTime);

    List<Auction> findByStatus(AuctionStatus status);

    Optional<Auction> findByIdAndStatus(Long id, AuctionStatus status);

    @Query("SELECT a FROM Auction a WHERE a.sellerId = :sellerId AND a.status = 'CLOSED'")
    List<Auction> findClosedAuctionsBySeller(@Param("sellerId") Long sellerId);

    @Query("SELECT a FROM Auction a WHERE a.highestBidderId = :bidderId AND a.status = 'CLOSED'")
    List<Auction> findWonAuctionsByBidder(@Param("bidderId") Long bidderId);

    List<Auction> findBySellerId(Long sellerId);

    @Query("SELECT a FROM Auction a WHERE a.endTime < :now AND a.status = 'ACTIVE'")
    List<Auction> findExpiredActiveAuctions(@Param("now") LocalDateTime now);
}