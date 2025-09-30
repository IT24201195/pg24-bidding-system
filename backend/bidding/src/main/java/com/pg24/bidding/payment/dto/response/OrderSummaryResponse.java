package com.pg24.bidding.payment.dto.response;

import com.pg24.bidding.payment.model.enums.OrderStatus;
import java.math.BigDecimal;

public class OrderSummaryResponse {
    private Long id;
    private Long auctionId;
    private Long buyerId;
    private BigDecimal totalAmount;
    private OrderStatus status;

    public OrderSummaryResponse(Long id, Long auctionId, Long buyerId, BigDecimal totalAmount, OrderStatus status) {
        this.id = id; this.auctionId = auctionId; this.buyerId = buyerId; this.totalAmount = totalAmount; this.status = status;
    }
    public Long getId() { return id; }
    public Long getAuctionId() { return auctionId; }
    public Long getBuyerId() { return buyerId; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public OrderStatus getStatus() { return status; }
}
