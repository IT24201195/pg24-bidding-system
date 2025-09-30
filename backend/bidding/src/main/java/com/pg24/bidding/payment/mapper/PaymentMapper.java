package com.pg24.bidding.payment.mapper;

import com.pg24.bidding.payment.dto.response.BankSlipInfoResponse;
import com.pg24.bidding.payment.dto.response.PaymentStatusResponse;
import com.pg24.bidding.payment.model.Payment;
import com.pg24.bidding.payment.model.PaymentSlip;

public class PaymentMapper {
    public static PaymentStatusResponse toStatus(Payment p) {
        return new PaymentStatusResponse(
                p.getId(),
                p.getOrder().getId(),
                p.getMethod(),
                p.getAmount(),
                p.getStatus(),
                p.getReviewerId(),
                p.getReviewReason(),
                p.getCreatedAt(),
                p.getUpdatedAt()
        );
    }
    public static BankSlipInfoResponse toSlip(PaymentSlip s) {
        return new BankSlipInfoResponse(
                s.getId(), s.getFilename(), s.getMimeType(), s.getSizeBytes(), s.getStoragePath(), s.getUploadedAt()
        );
    }
}
