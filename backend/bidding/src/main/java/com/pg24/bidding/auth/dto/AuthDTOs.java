package com.pg24.bidding.auth.dto;

public class AuthDTOs {
    public record RegisterRequest(String username, String password, String role) {}
    public record LoginRequest(String username, String password) {}
    public record LoginResponse(Long userId, String username, String role, String token) {}
}
