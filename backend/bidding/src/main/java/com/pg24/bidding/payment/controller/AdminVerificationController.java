package com.pg24.bidding.payment.controller;

import com.pg24.bidding.payment.dto.request.PaymentReviewRequest;
import com.pg24.bidding.payment.dto.response.PaymentStatusResponse;
import com.pg24.bidding.payment.mapper.PaymentMapper;
import com.pg24.bidding.payment.model.Payment;
import com.pg24.bidding.payment.model.enums.PaymentStatus;
import com.pg24.bidding.payment.service.PaymentService;
import com.pg24.bidding.payment.service.VerificationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/payments")
public class AdminVerificationController {
    private final VerificationService verification;
    private final PaymentService payments;

    public AdminVerificationController(VerificationService verification, PaymentService payments) {
        this.verification = verification; this.payments = payments;
    }

    @GetMapping
    public List<PaymentStatusResponse> pending(@RequestParam(defaultValue = "UNDER_REVIEW") PaymentStatus status) {
        List<Payment> list = payments.listByStatus(status);
        return list.stream().map(PaymentMapper::toStatus).collect(Collectors.toList());
    }

    @PostMapping("/{id}/review")
    public PaymentStatusResponse review(@PathVariable Long id, @Valid @RequestBody PaymentReviewRequest req) {
        Payment p = verification.review(id, req);
        return PaymentMapper.toStatus(p);
    }
}
