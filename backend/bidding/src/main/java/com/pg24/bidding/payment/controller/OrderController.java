package com.pg24.bidding.payment.controller;

import com.pg24.bidding.payment.dto.request.CheckoutRequest;
import com.pg24.bidding.payment.dto.request.CreateOrderRequest;
import com.pg24.bidding.payment.dto.response.OrderDetailResponse;
import com.pg24.bidding.payment.dto.response.OrderSummaryResponse;
import com.pg24.bidding.payment.mapper.OrderMapper;
import com.pg24.bidding.payment.model.Order;
import com.pg24.bidding.payment.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orders;

    public OrderController(OrderService orders) { this.orders = orders; }

    @PostMapping
    public ResponseEntity<OrderDetailResponse> create(@Valid @RequestBody CreateOrderRequest req) {
        Order created = orders.createFromWinner(req);
        return ResponseEntity.created(URI.create("/api/orders/" + created.getId()))
                .body(OrderMapper.toDetail(created));
    }

    @GetMapping("/{id}")
    public OrderDetailResponse get(@PathVariable Long id) {
        return OrderMapper.toDetail(orders.getById(id));
    }

    @GetMapping("/me")
    public List<OrderSummaryResponse> listMine(@RequestParam Long buyerId) {
        return orders.listMine(buyerId).stream().map(OrderMapper::toSummary).collect(Collectors.toList());
    }

    @PutMapping("/{id}/checkout")
    public OrderDetailResponse updateCheckout(@PathVariable Long id, @Valid @RequestBody CheckoutRequest req,
                                              @RequestParam Long buyerId) {
        Order updated = orders.updateCheckout(id, req, buyerId);
        return OrderMapper.toDetail(updated);
    }
}
