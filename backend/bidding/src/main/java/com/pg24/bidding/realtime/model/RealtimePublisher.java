package com.pg24.bidding.realtime.model;

import com.pg24.bidding.realtime.dto.HighestBidDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RealtimePublisher {
    private final SimpMessagingTemplate messaging;
    public RealtimePublisher(SimpMessagingTemplate messaging){this.messaging=messaging;}

    public void publishHighestBid(Long auctionId, BigDecimal amount, String maskedBidder){
        messaging.convertAndSend("/topic/auction." + auctionId + ".highest",
                new HighestBidDTO(auctionId, amount, maskedBidder));
    }
}
