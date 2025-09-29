package com.pg24.bidding.auth.controller;

import com.pg24.bidding.auth.dto.AuthDTOs.*;
import com.pg24.bidding.auth.model.User;
import com.pg24.bidding.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService service;
    public AuthController(AuthService service){this.service=service;}

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest req){
        return ResponseEntity.ok(service.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req){
        return ResponseEntity.ok(service.login(req));
    }
}
