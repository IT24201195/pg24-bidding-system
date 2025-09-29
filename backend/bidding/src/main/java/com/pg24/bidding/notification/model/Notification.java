package com.pg24.bidding.notification.model;

import com.pg24.bidding.auth.model.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @Column(nullable = false, length = 1000)
    private String message;

    private boolean readFlag = false;
    private LocalDateTime createdAt = LocalDateTime.now();

    // getters/setters
    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public User getUser(){return user;}
    public void setUser(User user){this.user=user;}
    public String getMessage(){return message;}
    public void setMessage(String message){this.message=message;}
    public boolean isReadFlag(){return readFlag;}
    public void setReadFlag(boolean readFlag){this.readFlag=readFlag;}
    public LocalDateTime getCreatedAt(){return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt){this.createdAt=createdAt;}
}
