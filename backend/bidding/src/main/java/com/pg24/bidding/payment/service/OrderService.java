package com.pg24.bidding.payment.service;

import com.pg24.bidding.payment.dto.request.CheckoutRequest;
import com.pg24.bidding.payment.dto.request.CreateOrderRequest;
import com.pg24.bidding.payment.exception.OrderNotFoundException;
import com.pg24.bidding.payment.model.Order;
import com.pg24.bidding.payment.model.enums.OrderStatus;
import com.pg24.bidding.payment.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orders;

    public OrderService(OrderRepository orders) { this.orders = orders; }

    public Order createFromWinner(CreateOrderRequest req) {
        Order o = new Order();
        o.setAuctionId(req.getAuctionId());
        o.setBuyerId(req.getBuyerId());
        o.setTotalAmount(req.getTotalAmount());
        o.setStatus(OrderStatus.PENDING_PAYMENT);
        return orders.save(o);
    }

    @Transactional(readOnly = true)
    public Order getById(Long id) {
        return orders.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Order> listMine(Long buyerId) {
        return orders.findByBuyerIdOrderByCreatedAtDesc(buyerId);
    }

    public Order updateCheckout(Long orderId, CheckoutRequest req, Long buyerId) {
        Order o = getById(orderId);
        if (!o.getBuyerId().equals(buyerId)) throw new OrderNotFoundException(orderId); // hide details
        o.setAddress(req.getAddress());
        o.setPhone(req.getPhone());
        o.setNotes(req.getNotes());
        return orders.save(o);
    }

    public void markPaid(Order order) {
        order.setStatus(OrderStatus.PAID);
        orders.save(order);
    }
}
