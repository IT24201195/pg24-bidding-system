package com.pg24.bidding.payment.dto.response;

import com.pg24.bidding.payment.model.enums.OrderStatus;
import java.math.BigDecimal;

public class OrderDetailResponse {
    private Long id;
    private Long auctionId;
    private Long buyerId;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private String address;
    private String phone;
    private String notes;

    public OrderDetailResponse(Long id, Long auctionId, Long buyerId, BigDecimal totalAmount,
                               OrderStatus status, String address, String phone, String notes) {
        this.id = id; this.auctionId = auctionId; this.buyerId = buyerId; this.totalAmount = totalAmount;
        this.status = status; this.address = address; this.phone = phone; this.notes = notes;
    }
    public Long getId() { return id; }
    public Long getAuctionId() { return auctionId; }
    public Long getBuyerId() { return buyerId; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public OrderStatus getStatus() { return status; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getNotes() { return notes; }
}
