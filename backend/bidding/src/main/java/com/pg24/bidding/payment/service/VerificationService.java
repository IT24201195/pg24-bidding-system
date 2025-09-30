package com.pg24.bidding.payment.service;

import com.pg24.bidding.payment.dto.request.PaymentReviewRequest;
import com.pg24.bidding.payment.exception.PaymentNotFoundException;
import com.pg24.bidding.payment.model.Payment;
import com.pg24.bidding.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VerificationService {
    private final PaymentRepository payments;
    private final PaymentService paymentService;
    private final OrderService orderService;

    public VerificationService(PaymentRepository payments, PaymentService paymentService, OrderService orderService) {
        this.payments = payments; this.paymentService = paymentService; this.orderService = orderService;
    }

    public Payment review(Long paymentId, PaymentReviewRequest req) {
        Payment p = payments.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException(paymentId));
        if (Boolean.TRUE.equals(req.getApprove())) {
            return paymentService.approve(p, req.getReviewerId(), req.getReason(), orderService);
        } else {
            return paymentService.reject(p, req.getReviewerId(), req.getReason());
        }
    }
}
