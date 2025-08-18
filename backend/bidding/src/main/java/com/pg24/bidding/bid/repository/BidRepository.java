package com.pg24.bidding.bid.repository;
import com.pg24.bidding.bid.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
public interface BidRepository extends JpaRepository<Bid, Long> {}
