package com.pg24.bidding.payment.service;

import com.pg24.bidding.payment.dto.request.PaymentInitiateRequest;
import com.pg24.bidding.payment.exception.InvalidPaymentStateException;
import com.pg24.bidding.payment.exception.OrderNotFoundException;
import com.pg24.bidding.payment.exception.PaymentNotFoundException;
import com.pg24.bidding.payment.model.Order;
import com.pg24.bidding.payment.model.Payment;
import com.pg24.bidding.payment.model.enums.PaymentStatus;
import com.pg24.bidding.payment.repository.OrderRepository;
import com.pg24.bidding.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class PaymentService {
    private final PaymentRepository payments;
    private final OrderRepository orders;

    public PaymentService(PaymentRepository payments, OrderRepository orders) {
        this.payments = payments; this.orders = orders;
    }

    public Payment initiate(PaymentInitiateRequest req) {
        Order order = orders.findById(req.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(req.getOrderId()));

        // Ownership check (simple)
        if (!order.getBuyerId().equals(req.getPayerId()))
            throw new InvalidPaymentStateException("Payer does not own this order");

        // Amount must match (can extend to include delivery fees)
        if (order.getTotalAmount().compareTo(req.getAmount()) != 0)
            throw new InvalidPaymentStateException("Amount must equal order total");

        // Idempotency: if there is an open payment, reuse or block
        payments.findTopByOrderIdOrderByCreatedAtDesc(order.getId()).ifPresent(p -> {
            if (p.getStatus() == PaymentStatus.INITIATED || p.getStatus() == PaymentStatus.UNDER_REVIEW)
                throw new InvalidPaymentStateException("There is already an active payment for this order");
        });

        Payment p = new Payment();
        p.setOrder(order);
        p.setMethod(req.getMethod());
        p.setAmount(req.getAmount());
        p.setStatus(PaymentStatus.INITIATED);
        return payments.save(p);
    }

    @Transactional(readOnly = true)
    public Payment getById(Long id) {
        return payments.findById(id).orElseThrow(() -> new PaymentNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Payment> listByStatus(PaymentStatus status) {
        return payments.findByStatusOrderByCreatedAtAsc(status);
    }

    @Transactional(readOnly = true)
    public List<Payment> listByOrder(Long orderId) {
        return payments.findByOrderId(orderId);
    }

    public Payment moveToUnderReview(Payment payment) {
        if (payment.getStatus() != PaymentStatus.INITIATED)
            throw new InvalidPaymentStateException("Only INITIATED can move to UNDER_REVIEW");
        payment.setStatus(PaymentStatus.UNDER_REVIEW);
        return payments.save(payment);
    }

    public Payment approve(Payment payment, Long reviewerId, String reason, OrderService orderService) {
        if (payment.getStatus() != PaymentStatus.UNDER_REVIEW)
            throw new InvalidPaymentStateException("Only UNDER_REVIEW can be approved");
        payment.setStatus(PaymentStatus.APPROVED);
        payment.setReviewerId(reviewerId);
        payment.setReviewReason(reason);
        Payment saved = payments.save(payment);
        orderService.markPaid(payment.getOrder());
        return saved;
    }

    public Payment reject(Payment payment, Long reviewerId, String reason) {
        if (payment.getStatus() != PaymentStatus.UNDER_REVIEW)
            throw new InvalidPaymentStateException("Only UNDER_REVIEW can be rejected");
        payment.setStatus(PaymentStatus.REJECTED);
        payment.setReviewerId(reviewerId);
        payment.setReviewReason(reason);
        return payments.save(payment);
    }
}
