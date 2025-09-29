package com.pg24.bidding.realtime.model;

import com.pg24.bidding.realtime.dto.HighestBidDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class RealtimePublisher {
    private final SimpMessagingTemplate broker;

    public RealtimePublisher(SimpMessagingTemplate broker) {
        this.broker = broker;
    }

    public void publishHighestBid(Long auctionId, java.math.BigDecimal amount) {
        broker.convertAndSend("/topic/auctions/" + auctionId + "/highestBid",
                new HighestBidDTO(auctionId, amount));
    }
}
