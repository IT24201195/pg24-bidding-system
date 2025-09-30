package com.pg24.bidding.payment.repository;

import com.pg24.bidding.payment.model.PaymentSlip;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentSlipRepository extends JpaRepository<PaymentSlip, Long> {
    List<PaymentSlip> findByPaymentId(Long paymentId);
}
