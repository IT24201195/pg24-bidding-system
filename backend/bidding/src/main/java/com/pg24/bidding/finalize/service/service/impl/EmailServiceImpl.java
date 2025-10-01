package com.example.Bidding.System.service.impl;

import com.example.Bidding.System.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Override
    public void sendWinnerNotification(Long winnerId, String auctionTitle, Double winningBid) {
        sendEmailOrLog(winnerId, "You won the auction!", "Congratulations! You won '" + auctionTitle + "' with $" + winningBid);
    }

    @Override
    public void sendSellerNotification(Long sellerId, String auctionTitle, Double winningBid, Long winnerId) {
        sendEmailOrLog(sellerId, "Your auction has ended", "Auction '" + auctionTitle + "' sold for $" + winningBid + ". Winner ID: " + winnerId);
    }

    @Override
    public void sendNoWinnerNotification(Long sellerId, String auctionTitle) {
        sendEmailOrLog(sellerId, "Your auction ended with no winner", "Auction '" + auctionTitle + "' received no winning bids.");
    }

    @Override
    public void sendOutbidNotification(Long bidderId, String auctionTitle, Double newBid) {
        sendEmailOrLog(bidderId, "You've been outbid", "A higher bid of $" + newBid + " was placed on '" + auctionTitle + "'.");
    }

    private void sendEmailOrLog(Long userId, String subject, String body) {
        if (mailSender == null) {
            log.info("[EMAIL-FAKE] toUserId={} subject='{}' body='{}'", userId, subject, body);
            return;
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("user" + userId + "@example.com");
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (Exception ex) {
            log.warn("Failed to send email via JavaMailSender, falling back to log. cause={}", ex.getMessage());
            log.info("[EMAIL-FAKE] toUserId={} subject='{}' body='{}'", userId, subject, body);
        }
    }
}


