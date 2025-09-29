package com.pg24.bidding.order.repository;

import com.pg24.bidding.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByAuction_Id(Long auctionId);
}
