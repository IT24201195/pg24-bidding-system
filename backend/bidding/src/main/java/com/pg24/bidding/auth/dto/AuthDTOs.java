package com.pg24.bidding.auth.dto;

import com.pg24.bidding.auth.model.User.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public class AuthDTOs {
    public record RegisterRequest(@Email String email, @NotBlank String password, String fullName,
                                  Set<UserRole> roles) {}
    public record LoginRequest(@Email String email, @NotBlank String password) {}
    public record AuthResponse(String token) {}
}
