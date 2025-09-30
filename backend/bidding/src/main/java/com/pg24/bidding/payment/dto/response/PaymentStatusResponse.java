package com.pg24.bidding.payment.dto.response;

import com.pg24.bidding.payment.model.enums.PaymentMethod;
import com.pg24.bidding.payment.model.enums.PaymentStatus;
import java.math.BigDecimal;
import java.time.Instant;

public class PaymentStatusResponse {
    private Long id;
    private Long orderId;
    private PaymentMethod method;
    private BigDecimal amount;
    private PaymentStatus status;
    private Long reviewerId;
    private String reviewReason;
    private Instant createdAt;
    private Instant updatedAt;

    public PaymentStatusResponse(Long id, Long orderId, PaymentMethod method, BigDecimal amount,
                                 PaymentStatus status, Long reviewerId, String reviewReason,
                                 Instant createdAt, Instant updatedAt) {
        this.id = id; this.orderId = orderId; this.method = method; this.amount = amount;
        this.status = status; this.reviewerId = reviewerId; this.reviewReason = reviewReason;
        this.createdAt = createdAt; this.updatedAt = updatedAt;
    }
    public Long getId() { return id; }
    public Long getOrderId() { return orderId; }
    public PaymentMethod getMethod() { return method; }
    public BigDecimal getAmount() { return amount; }
    public PaymentStatus getStatus() { return status; }
    public Long getReviewerId() { return reviewerId; }
    public String getReviewReason() { return reviewReason; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
