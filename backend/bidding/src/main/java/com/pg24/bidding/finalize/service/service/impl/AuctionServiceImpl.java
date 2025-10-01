package com.example.Bidding.System.service.impl;

import com.example.Bidding.System.dto.requset.CreateAuctionRequest;
import com.example.Bidding.System.dto.response.AuctionResponse;
import com.example.Bidding.System.dto.response.AuctionResultResponse;
import com.example.Bidding.System.entity.Auction;
import com.example.Bidding.System.entity.AuctionResult;
import com.example.Bidding.System.entity.Bid;
import com.example.Bidding.System.enums.AuctionStatus;
import com.example.Bidding.System.enums.NotificationType;
import com.example.Bidding.System.exception.AuctionNotFoundException;
import com.example.Bidding.System.repository.AuctionRepository;
import com.example.Bidding.System.repository.AuctionResultRepository;
import com.example.Bidding.System.repository.BidRepository;
import com.example.Bidding.System.service.AuctionService;
import com.example.Bidding.System.service.EmailService;
import com.example.Bidding.System.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuctionServiceImpl implements AuctionService {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private AuctionResultRepository auctionResultRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationService notificationService;

    @Override
    public Auction createAuction(CreateAuctionRequest request) {
        Auction auction = new Auction();
        auction.setTitle(request.getTitle());
        auction.setDescription(request.getDescription());
        auction.setStartingPrice(request.getStartingPrice());
        auction.setSellerId(request.getSellerId());
        auction.setEndTime(request.getEndTime());
        auction.setStatus(AuctionStatus.ACTIVE);

        return auctionRepository.save(auction);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuctionResponse> getAllAuctions() {
        return auctionRepository.findAll().stream()
                .map(this::convertToAuctionResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AuctionResponse getAuctionById(Long id) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new AuctionNotFoundException(id));
        return convertToAuctionResponse(auction);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuctionResponse> getAuctionsBySeller(Long sellerId) {
        return auctionRepository.findBySellerId(sellerId).stream()
                .map(this::convertToAuctionResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuctionResponse> getActiveAuctions() {
        return auctionRepository.findByStatus(AuctionStatus.ACTIVE).stream()
                .map(this::convertToAuctionResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void finalizeExpiredAuctions() {
        LocalDateTime now = LocalDateTime.now();
        List<Auction> expiredAuctions = auctionRepository.findExpiredActiveAuctions(now);

        for (Auction auction : expiredAuctions) {
            finalizeAuction(auction.getId());
        }
    }

    @Override
    public AuctionResult finalizeAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionNotFoundException(auctionId));

        if (auction.getStatus() != AuctionStatus.ACTIVE) {
            throw new IllegalStateException("Auction is already finalized");
        }

        // Find the highest bid
        Optional<Bid> highestBid = bidRepository.findTopByAuctionIdOrderByAmountDesc(auctionId);

        AuctionResult result = new AuctionResult();
        result.setAuctionId(auctionId);
        result.setSellerId(auction.getSellerId());

        if (highestBid.isPresent() && highestBid.get().getAmount() >= auction.getStartingPrice()) {
            // Auction has a valid winner
            Bid winningBid = highestBid.get();
            result.setWinnerId(winningBid.getBidderId());
            result.setWinningBid(winningBid.getAmount());

            // Update auction
            auction.setStatus(AuctionStatus.CLOSED);
            auction.setCurrentBid(winningBid.getAmount());
            auction.setHighestBidderId(winningBid.getBidderId());

            // Mark winning bid
            winningBid.setIsWinningBid(true);
            bidRepository.save(winningBid);

            // Send notifications
            emailService.sendWinnerNotification(winningBid.getBidderId(), auction.getTitle(), winningBid.getAmount());
            emailService.sendSellerNotification(auction.getSellerId(), auction.getTitle(), winningBid.getAmount(), winningBid.getBidderId());

            // Create system notifications
            notificationService.createNotification(
                    winningBid.getBidderId(),
                    "Auction Won!",
                    String.format("Congratulations! You won the auction '%s' with a bid of $%.2f",
                            auction.getTitle(), winningBid.getAmount()),
                    NotificationType.AUCTION_WON.toString(),
                    auctionId
            );

            notificationService.createNotification(
                    auction.getSellerId(),
                    "Auction Completed",
                    String.format("Your auction '%s' has been completed. Winning bid: $%.2f",
                            auction.getTitle(), winningBid.getAmount()),
                    NotificationType.AUCTION_CLOSED.toString(),
                    auctionId
            );

            result.setWinnerNotified(true);
            result.setSellerNotified(true);
        } else {
            // No valid bids - auction ends without winner
            auction.setStatus(AuctionStatus.EXPIRED);
            emailService.sendNoWinnerNotification(auction.getSellerId(), auction.getTitle());

            notificationService.createNotification(
                    auction.getSellerId(),
                    "Auction Expired",
                    String.format("Your auction '%s' has expired without any winning bids.", auction.getTitle()),
                    NotificationType.AUCTION_EXPIRED.toString(),
                    auctionId
            );

            result.setSellerNotified(true);
        }

        auctionRepository.save(auction);
        return auctionResultRepository.save(result);
    }

    @Override
    @Transactional(readOnly = true)
    public AuctionResultResponse getAuctionResult(Long auctionId) {
        AuctionResult result = auctionResultRepository.findByAuctionId(auctionId)
                .orElseThrow(() -> new AuctionNotFoundException("Auction result not found for id: " + auctionId));
        return convertToAuctionResultResponse(result);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuctionResultResponse> getSellerClosedAuctions(Long sellerId) {
        return auctionResultRepository.findBySellerId(sellerId).stream()
                .map(this::convertToAuctionResultResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuctionResultResponse> getBuyerWonAuctions(Long buyerId) {
        return auctionResultRepository.findByWinnerId(buyerId).stream()
                .map(this::convertToAuctionResultResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Auction updateAuctionStatus(Long auctionId, String status) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionNotFoundException(auctionId));

        try {
            AuctionStatus newStatus = AuctionStatus.valueOf(status.toUpperCase());
            auction.setStatus(newStatus);
            return auctionRepository.save(auction);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid auction status: " + status);
        }
    }

    private AuctionResponse convertToAuctionResponse(Auction auction) {
        AuctionResponse response = new AuctionResponse();
        response.setId(auction.getId());
        response.setTitle(auction.getTitle());
        response.setDescription(auction.getDescription());
        response.setStartingPrice(auction.getStartingPrice());
        response.setCurrentBid(auction.getCurrentBid());
        response.setSellerId(auction.getSellerId());
        response.setHighestBidderId(auction.getHighestBidderId());
        response.setStartTime(auction.getStartTime());
        response.setEndTime(auction.getEndTime());
        response.setStatus(auction.getStatus());
        response.setCreatedAt(auction.getCreatedAt());
        response.setUpdatedAt(auction.getUpdatedAt());

        // Calculate additional fields
        response.setBidCount(bidRepository.countBidsByAuctionId(auction.getId()));

        // Calculate time remaining
        if (auction.getStatus() == AuctionStatus.ACTIVE) {
            long minutesRemaining = ChronoUnit.MINUTES.between(LocalDateTime.now(), auction.getEndTime());
            if (minutesRemaining > 0) {
                long hours = minutesRemaining / 60;
                long minutes = minutesRemaining % 60;
                response.setTimeRemaining(String.format("%dh %dm", hours, minutes));
            } else {
                response.setTimeRemaining("Expired");
            }
        } else {
            response.setTimeRemaining("Closed");
        }

        return response;
    }

    private AuctionResultResponse convertToAuctionResultResponse(AuctionResult result) {
        AuctionResultResponse response = new AuctionResultResponse();
        response.setId(result.getId());
        response.setAuctionId(result.getAuctionId());
        response.setWinnerId(result.getWinnerId());
        response.setWinningBid(result.getWinningBid());
        response.setSellerId(result.getSellerId());
        response.setFinalizedAt(result.getFinalizedAt());
        response.setSellerNotified(result.getSellerNotified());
        response.setWinnerNotified(result.getWinnerNotified());
        response.setNotificationCount(result.getNotificationCount());

        // In a real application, you would fetch user names from user service
        if (result.getWinnerId() != null) {
            response.setWinnerName("User " + result.getWinnerId());
        }
        response.setSellerName("User " + result.getSellerId());

        // Fetch auction title
        auctionRepository.findById(result.getAuctionId()).ifPresent(auction -> {
            response.setAuctionTitle(auction.getTitle());
        });

        return response;
    }
}