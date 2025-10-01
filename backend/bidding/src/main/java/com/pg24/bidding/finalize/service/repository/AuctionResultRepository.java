package com.example.Bidding.System.repository;

import com.example.Bidding.System.entity.AuctionResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuctionResultRepository extends JpaRepository<AuctionResult, Long> {

    Optional<AuctionResult> findByAuctionId(Long auctionId);

    List<AuctionResult> findBySellerId(Long sellerId);

    List<AuctionResult> findByWinnerId(Long winnerId);

    List<AuctionResult> findBySellerNotifiedFalse();

    List<AuctionResult> findByWinnerNotifiedFalse();
}