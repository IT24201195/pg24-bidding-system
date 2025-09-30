package com.pg24.bidding.payment.exception;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(Long id) { super("Payment not found: " + id); }
}
