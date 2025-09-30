package com.pg24.bidding.payment.controller;

import com.pg24.bidding.payment.dto.request.PaymentInitiateRequest;
import com.pg24.bidding.payment.dto.response.BankSlipInfoResponse;
import com.pg24.bidding.payment.dto.response.PaymentStatusResponse;
import com.pg24.bidding.payment.mapper.PaymentMapper;
import com.pg24.bidding.payment.model.Payment;
import com.pg24.bidding.payment.model.PaymentSlip;
import com.pg24.bidding.payment.model.enums.PaymentStatus;
import com.pg24.bidding.payment.service.BankSlipService;
import com.pg24.bidding.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService payments;
    private final BankSlipService slips;

    public PaymentController(PaymentService payments, BankSlipService slips) {
        this.payments = payments; this.slips = slips;
    }

    @PostMapping
    public ResponseEntity<PaymentStatusResponse> initiate(@Valid @RequestBody PaymentInitiateRequest req) {
        Payment p = payments.initiate(req);
        return ResponseEntity.created(URI.create("/api/payments/" + p.getId()))
                .body(PaymentMapper.toStatus(p));
    }

    @GetMapping("/{id}")
    public PaymentStatusResponse get(@PathVariable Long id) {
        return PaymentMapper.toStatus(payments.getById(id));
    }

    @GetMapping("/status")
    public List<PaymentStatusResponse> listByStatus(@RequestParam PaymentStatus status) {
        return payments.listByStatus(status).stream().map(PaymentMapper::toStatus).collect(Collectors.toList());
    }

    @PostMapping("/{id}/slip")
    public BankSlipInfoResponse uploadSlip(@PathVariable Long id,
                                           @RequestParam("file") MultipartFile file,
                                           @RequestParam("uploaderId") Long uploaderId) {
        PaymentSlip saved = slips.upload(id, file, uploaderId);
        return PaymentMapper.toSlip(saved);
    }

    @GetMapping("/{id}/slips")
    public List<BankSlipInfoResponse> listSlips(@PathVariable Long id) {
        List<PaymentSlip> list = slips.listForPayment(id);
        return list.stream().map(PaymentMapper::toSlip).collect(Collectors.toList());
    }
}
