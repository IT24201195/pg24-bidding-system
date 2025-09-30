package com.pg24.bidding.payment.mapper;

import com.pg24.bidding.payment.dto.response.OrderDetailResponse;
import com.pg24.bidding.payment.dto.response.OrderSummaryResponse;
import com.pg24.bidding.payment.model.Order;

public class OrderMapper {
    public static OrderSummaryResponse toSummary(Order o) {
        return new OrderSummaryResponse(o.getId(), o.getAuctionId(), o.getBuyerId(), o.getTotalAmount(), o.getStatus());
    }
    public static OrderDetailResponse toDetail(Order o) {
        return new OrderDetailResponse(o.getId(), o.getAuctionId(), o.getBuyerId(), o.getTotalAmount(),
                o.getStatus(), o.getAddress(), o.getPhone(), o.getNotes());
    }
}
