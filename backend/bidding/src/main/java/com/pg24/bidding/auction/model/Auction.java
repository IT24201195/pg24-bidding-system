package com.pg24.bidding.auction.model;

import com.pg24.bidding.auction.controller.AuctionStatus;
import com.pg24.bidding.auth.model.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Auction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) private User seller;

    private String title;
    @Column(length = 1000) private String description;

    private BigDecimal basePrice = BigDecimal.ZERO;
    private BigDecimal minIncrement = BigDecimal.valueOf(100);

    private LocalDateTime startAt;
    private LocalDateTime endAt;

    @Enumerated(EnumType.STRING)
    private AuctionStatus status = AuctionStatus.ACTIVE;

    @Version
    private Long version;

    // getters/setters
    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public User getSeller(){return seller;}
    public void setSeller(User seller){this.seller=seller;}
    public String getTitle(){return title;}
    public void setTitle(String title){this.title=title;}
    public String getDescription(){return description;}
    public void setDescription(String description){this.description=description;}
    public BigDecimal getBasePrice(){return basePrice;}
    public void setBasePrice(BigDecimal basePrice){this.basePrice=basePrice;}
    public BigDecimal getMinIncrement(){return minIncrement;}
    public void setMinIncrement(BigDecimal minIncrement){this.minIncrement=minIncrement;}
    public LocalDateTime getStartAt(){return startAt;}
    public void setStartAt(LocalDateTime startAt){this.startAt=startAt;}
    public LocalDateTime getEndAt(){return endAt;}
    public void setEndAt(LocalDateTime endAt){this.endAt=endAt;}
    public AuctionStatus getStatus(){return status;}
    public void setStatus(AuctionStatus status){this.status=status;}
    public Long getVersion(){return version;}
    public void setVersion(Long version){this.version=version;}
}
