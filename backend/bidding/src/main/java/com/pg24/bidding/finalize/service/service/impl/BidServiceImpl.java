package com.example.Bidding.System.service.impl;

import com.example.Bidding.System.dto.requset.PlaceBidRequest;
import com.example.Bidding.System.dto.response.BidResponse;
import com.example.Bidding.System.entity.Auction;
import com.example.Bidding.System.entity.Bid;
import com.example.Bidding.System.enums.AuctionStatus;
import com.example.Bidding.System.enums.NotificationType;
import com.example.Bidding.System.exception.AuctionNotFoundException;
import com.example.Bidding.System.exception.BidNotFoundException;
import com.example.Bidding.System.exception.InvalidBidException;
import com.example.Bidding.System.repository.AuctionRepository;
import com.example.Bidding.System.repository.BidRepository;
import com.example.Bidding.System.service.BidService;
import com.example.Bidding.System.service.EmailService;
import com.example.Bidding.System.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BidServiceImpl implements BidService {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationService notificationService;

    @Override
    public Bid placeBid(PlaceBidRequest request) {
        validateBid(request);

        Auction auction = auctionRepository.findById(request.getAuctionId())
                .orElseThrow(() -> new AuctionNotFoundException(request.getAuctionId()));

        // Check if auction is active
        if (auction.getStatus() != AuctionStatus.ACTIVE) {
            throw new InvalidBidException("Cannot bid on inactive auction");
        }

        // Check if auction has ended
        if (auction.getEndTime().isBefore(java.time.LocalDateTime.now())) {
            throw new InvalidBidException("Auction has already ended");
        }

        Bid bid = new Bid();
        bid.setAuctionId(request.getAuctionId());
        bid.setBidderId(request.getBidderId());
        bid.setAmount(request.getAmount());

        // Update auction current bid and highest bidder
        auction.setCurrentBid(request.getAmount());
        auction.setHighestBidderId(request.getBidderId());
        auctionRepository.save(auction);

        // Notify previous highest bidder if they were outbid
        Optional<Bid> previousHighestBid = bidRepository.findTopByAuctionIdOrderByAmountDesc(request.getAuctionId());
        if (previousHighestBid.isPresent() &&
                !previousHighestBid.get().getBidderId().equals(request.getBidderId()) &&
                previousHighestBid.get().getAmount() < request.getAmount()) {

            emailService.sendOutbidNotification(
                    previousHighestBid.get().getBidderId(),
                    auction.getTitle(),
                    request.getAmount()
            );

            notificationService.createNotification(
                    previousHighestBid.get().getBidderId(),
                    "You've been outbid!",
                    String.format("Someone placed a higher bid of $%.2f on '%s'",
                            request.getAmount(), auction.getTitle()),
                    NotificationType.OUTBID.toString(),
                    request.getAuctionId()
            );
        }

        return bidRepository.save(bid);
    }

    @Override
    public void validateBid(PlaceBidRequest request) {
        Auction auction = auctionRepository.findById(request.getAuctionId())
                .orElseThrow(() -> new AuctionNotFoundException(request.getAuctionId()));

        // Check if bid is higher than starting price
        if (request.getAmount() <= auction.getStartingPrice()) {
            throw new InvalidBidException("Bid must be higher than starting price: $" + auction.getStartingPrice());
        }

        // Check if bid is higher than current bid
        Optional<Double> currentMaxBid = bidRepository.findMaxBidAmountByAuctionId(request.getAuctionId());
        if (currentMaxBid.isPresent() && request.getAmount() <= currentMaxBid.get()) {
            throw new InvalidBidException("Bid must be higher than current highest bid: $" + currentMaxBid.get());
        }

        // Check if bidder is not the seller
        if (request.getBidderId().equals(auction.getSellerId())) {
            throw new InvalidBidException("Seller cannot bid on their own auction");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<BidResponse> getBidsByAuction(Long auctionId) {
        if (!auctionRepository.existsById(auctionId)) {
            throw new AuctionNotFoundException(auctionId);
        }

        return bidRepository.findByAuctionIdOrderByAmountDesc(auctionId).stream()
                .map(this::convertToBidResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BidResponse> getBidsByBidder(Long bidderId) {
        return bidRepository.findByBidderId(bidderId).stream()
                .map(this::convertToBidResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BidResponse getHighestBid(Long auctionId) {
        Bid bid = bidRepository.findTopByAuctionIdOrderByAmountDesc(auctionId)
                .orElseThrow(() -> new BidNotFoundException("No bids found for auction: " + auctionId));
        return convertToBidResponse(bid);
    }

    private BidResponse convertToBidResponse(Bid bid) {
        BidResponse response = new BidResponse();
        response.setId(bid.getId());
        response.setAuctionId(bid.getAuctionId());
        response.setBidderId(bid.getBidderId());
        response.setAmount(bid.getAmount());
        response.setBidTime(bid.getBidTime());
        response.setIsWinningBid(bid.getIsWinningBid());

        // In a real application, you would fetch bidder name from user service
        response.setBidderName("User " + bid.getBidderId());

        return response;
    }
}