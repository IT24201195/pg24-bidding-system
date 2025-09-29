package com.pg24.bidding.auth.service;

import com.pg24.bidding.auth.dto.AuthDTOs.*;
import com.pg24.bidding.auth.model.User;
import com.pg24.bidding.auth.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository repo;
    public AuthService(UserRepository repo){this.repo=repo;}

    @Transactional
    public User register(RegisterRequest req){
        var u = new User();
        u.setUsername(req.username());
        u.setPassword(req.password());
        u.setRole(req.role());
        return repo.save(u);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest req){
        var user = repo.findByUsername(req.username())
                .filter(u -> u.getPassword().equals(req.password()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        String token = UUID.randomUUID().toString(); // demo token
        return new LoginResponse(user.getId(), user.getUsername(), user.getRole(), token);
    }
}
