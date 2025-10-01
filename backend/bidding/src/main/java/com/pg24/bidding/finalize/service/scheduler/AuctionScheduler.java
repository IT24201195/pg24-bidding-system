package com.example.Bidding.System.scheduler;

import com.example.Bidding.System.service.AuctionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AuctionScheduler {

    private static final Logger logger = LoggerFactory.getLogger(AuctionScheduler.class);

    @Autowired
    private AuctionService auctionService;

    // Run every minute to check for expired auctions
    @Scheduled(fixedRate = 60000) // 60,000 ms = 1 minute
    public void finalizeExpiredAuctions() {
        try {
            logger.info("Starting scheduled auction finalization...");
            auctionService.finalizeExpiredAuctions();
            logger.info("Scheduled auction finalization completed successfully");
        } catch (Exception e) {
            logger.error("Error during scheduled auction finalization: {}", e.getMessage(), e);
        }
    }

    // Run every hour to clean up old data (optional)
    @Scheduled(cron = "0 0 * * * *") // Every hour at minute 0
    public void cleanupOldData() {
        try {
            logger.info("Starting scheduled data cleanup...");
            // Add cleanup logic here if needed
            logger.info("Scheduled data cleanup completed successfully");
        } catch (Exception e) {
            logger.error("Error during scheduled data cleanup: {}", e.getMessage(), e);
        }
    }
}