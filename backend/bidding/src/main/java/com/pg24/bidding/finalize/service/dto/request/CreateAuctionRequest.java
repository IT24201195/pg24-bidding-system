package com.example.Bidding.System.dto.requset;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class CreateAuctionRequest {
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 10, message = "Description must be at least 10 characters")
    private String description;

    @NotNull(message = "Starting price is required")
    @Positive(message = "Starting price must be positive")
    private Double startingPrice;

    @NotNull(message = "End time is required")
    @Future(message = "End time must be in the future")
    private LocalDateTime endTime;

    @NotNull(message = "Seller ID is required")
    private Long sellerId;

    // Constructors
    public CreateAuctionRequest() {}

    public CreateAuctionRequest(String title, String description, Double startingPrice, LocalDateTime endTime, Long sellerId) {
        this.title = title;
        this.description = description;
        this.startingPrice = startingPrice;
        this.endTime = endTime;
        this.sellerId = sellerId;
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getStartingPrice() { return startingPrice; }
    public void setStartingPrice(Double startingPrice) { this.startingPrice = startingPrice; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }
}