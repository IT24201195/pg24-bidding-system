package com.pg24.bidding.finalize.service;

import com.pg24.bidding.auction.controller.AuctionStatus;
import com.pg24.bidding.auction.model.Auction;
import com.pg24.bidding.auction.repository.AuctionRepository;
import com.pg24.bidding.bid.repository.BidRepository;
import com.pg24.bidding.notification.service.NotificationService;
import com.pg24.bidding.order.model.Order;
import com.pg24.bidding.order.model.Order.PaymentStatus;
import com.pg24.bidding.order.repository.OrderRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FinalizeService {

    private final AuctionRepository auctions;
    private final BidRepository bids;
    private final OrderRepository orders;
    private final NotificationService notifications;

    public FinalizeService(AuctionRepository auctions,
                           BidRepository bids,
                           OrderRepository orders,
                           NotificationService notifications) {
        this.auctions = auctions;
        this.bids = bids;
        this.orders = orders;
        this.notifications = notifications;
    }

    /**
     * Runs every 30 seconds:
     *  - Finds ACTIVE auctions whose endAt has passed
     *  - Marks them CLOSED
     *  - If a highest bid exists and no Order yet -> creates PENDING Order
     *  - Notifies winner and seller
     */
    @Scheduled(fixedDelay = 30000)
    @Transactional
    public void autoCloseAuctions() {
        List<Auction> toClose = auctions.findByStatusAndEndAtBefore(AuctionStatus.ACTIVE, LocalDateTime.now());
        for (Auction a : toClose) {
            var top = bids.findTopByAuction_IdOrderByAmountDesc(a.getId()).orElse(null);

            // Close auction
            a.setStatus(AuctionStatus.CLOSED);

            // If already ordered, skip
            if (orders.existsByAuction_Id(a.getId())) continue;

            if (top != null) {
                // Create order
                Order o = new Order();
                o.setAuction(a);
                o.setBuyer(top.getBidder());
                o.setWinningBid(top);
                o.setPaymentStatus(PaymentStatus.PENDING);
                o.setCreatedAt(LocalDateTime.now());
                orders.save(o);

                // Notifications
                notifications.notify(top.getBidder(),
                        "🎉 Congratulations! You won the auction '" + a.getTitle() + "'. Order #" + o.getId());
                notifications.notify(a.getSeller(),
                        "✅ Your auction '" + a.getTitle() + "' ended. Winner: " + top.getBidder().getEmail());
            } else {
                // No bids; optionally notify seller that auction ended with no winner
                notifications.notify(a.getSeller(),
                        "⚠️ Your auction '" + a.getTitle() + "' ended with no bids.");
            }
        }
    }
}
