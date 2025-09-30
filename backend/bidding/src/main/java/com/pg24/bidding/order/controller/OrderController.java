package com.pg24.bidding.order.controller;

import com.pg24.bidding.order.model.Order;
import com.pg24.bidding.order.model.Order.PaymentStatus;
import com.pg24.bidding.order.repository.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderRepository orders;

    public OrderController(OrderRepository orders) { this.orders = orders; }

    @GetMapping("/{id}")
    public Order get(@PathVariable Long id) {
        return orders.findById(id).orElseThrow();
    }

    @PostMapping("/{id}/mark-paid")
    public ResponseEntity<?> markPaid(@PathVariable Long id) {
        Order o = orders.findById(id).orElseThrow();
        o.setPaymentStatus(PaymentStatus.PAID);
        orders.save(o);
        return ResponseEntity.ok(Map.of("status", "PAID"));
    }
}
