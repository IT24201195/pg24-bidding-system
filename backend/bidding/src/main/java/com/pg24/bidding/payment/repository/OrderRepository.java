package com.pg24.bidding.payment.repository;

import com.pg24.bidding.payment.model.Order;
import com.pg24.bidding.payment.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByBuyerIdOrderByCreatedAtDesc(Long buyerId);
    Optional<Order> findTopByAuctionIdAndBuyerIdAndStatus(Long auctionId, Long buyerId, OrderStatus status);
}
