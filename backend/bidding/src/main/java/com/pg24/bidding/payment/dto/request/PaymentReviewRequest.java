package com.pg24.bidding.payment.dto.request;

import jakarta.validation.constraints.NotNull;

public class PaymentReviewRequest {
    @NotNull
    private Boolean approve;
    private String reason;
    @NotNull
    private Long reviewerId;

    public Boolean getApprove() { return approve; }
    public void setApprove(Boolean approve) { this.approve = approve; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Long getReviewerId() { return reviewerId; }
    public void setReviewerId(Long reviewerId) { this.reviewerId = reviewerId; }
}
