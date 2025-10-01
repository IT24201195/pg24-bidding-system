package com.pg24.bidding.auth.controller;

import com.pg24.bidding.auth.dto.AuthDTOs.*;
import com.pg24.bidding.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService auth;
    private final AuthenticationManager authManager;

    public AuthController(AuthService auth, AuthenticationManager authManager) {
        this.auth = auth;
        this.authManager = authManager;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
        var u = auth.register(req.email(), req.password(), req.fullName(), req.roles());
        String token = auth.tokenFor(u.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password()));
        String token = auth.tokenFor(req.email());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
