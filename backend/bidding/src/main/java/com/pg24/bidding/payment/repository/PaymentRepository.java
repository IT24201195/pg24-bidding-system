package com.pg24.bidding.payment.repository;

import com.pg24.bidding.payment.model.Payment;
import com.pg24.bidding.payment.model.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findTopByOrderIdOrderByCreatedAtDesc(Long orderId);
    List<Payment> findByOrderId(Long orderId);
    List<Payment> findByStatusOrderByCreatedAtAsc(PaymentStatus status);
}
