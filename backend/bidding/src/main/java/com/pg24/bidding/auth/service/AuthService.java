package com.pg24.bidding.auth.service;

import com.pg24.bidding.auth.model.User;
import com.pg24.bidding.auth.repository.UserRepository;
import com.pg24.bidding.common.Security.JwtUtil;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService implements UserDetailsService {

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;

    public AuthService(UserRepository users, PasswordEncoder encoder, JwtUtil jwt) {
        this.users = users;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    public User register(String email, String password, String fullName, Set<User.UserRole> roles) {
        if (users.existsByEmail(email)) throw new IllegalStateException("Email already exists");
        User u = new User();
        u.setEmail(email);
        u.setPassword(encoder.encode(password));
        u.setFullName(fullName);
        u.setRoles(roles == null || roles.isEmpty() ? Set.of(User.UserRole.BUYER) : roles);
        return users.save(u);
    }

    public String tokenFor(String email) {
        var u = users.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return jwt.generateToken(u.getEmail(), java.util.Map.of("roles", u.getRoles()));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = users.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var authorities = u.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                .collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(u.getEmail(), u.getPassword(), authorities);
    }

    public User getByEmail(String email) { return users.findByEmail(email).orElseThrow(); }
}
