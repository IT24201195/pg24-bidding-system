package com.bidding.controller;

import com.bidding.dto.ApiResponse;
import com.bidding.dto.LoginRequest;
import com.bidding.dto.RegisterRequest;
import com.bidding.model.User;
import com.bidding.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error retrieving users: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id) {
        try {
            Optional<User> user = userService.getUserById(id);
            if (user.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user.get()));
            } else {
                return ResponseEntity.ok(ApiResponse.error("User not found with id: " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error retrieving user: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            // Check if username already exists
            if (userService.usernameExists(registerRequest.getUsername())) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Username already exists"));
            }

            // Check if email already exists
            if (userService.emailExists(registerRequest.getEmail())) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Email already exists"));
            }

            User user = new User(
                    registerRequest.getUsername(),
                    registerRequest.getEmail(),
                    registerRequest.getPassword(),
                    registerRequest.getFirstName(),
                    registerRequest.getLastName()
            );

            User createdUser = userService.createUser(user);
            return ResponseEntity.ok(ApiResponse.success("User registered successfully", createdUser));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error registering user: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            boolean isValid = userService.validateUser(loginRequest.getUsername(), loginRequest.getPassword());
            if (isValid) {
                return ResponseEntity.ok(ApiResponse.success("Login successful"));
            } else {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Invalid username or password"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error during login: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(ApiResponse.success("User updated successfully", updatedUser));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error updating user: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error deleting user: " + e.getMessage()));
        }
    }
}