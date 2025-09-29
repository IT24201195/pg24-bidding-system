package com.pg24.bidding.auction.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Auction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(length = 1000)
    private String description;

    private BigDecimal basePrice = BigDecimal.ZERO;
    private LocalDateTime endAt;

    public boolean isEnded(){
        return endAt != null && LocalDateTime.now().isAfter(endAt);
    }

    // getters/setters
    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public String getTitle(){return title;}
    public void setTitle(String title){this.title=title;}
    public String getDescription(){return description;}
    public void setDescription(String description){this.description=description;}
    public BigDecimal getBasePrice(){return basePrice;}
    public void setBasePrice(BigDecimal basePrice){this.basePrice=basePrice;}
    public LocalDateTime getEndAt(){return endAt;}
    public void setEndAt(LocalDateTime endAt){this.endAt=endAt;}
}
