package com.pg24.bidding.auth.controller;

import com.bidding.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardData() {
        try {
            Map<String, Object> dashboardData = new HashMap<>();
            dashboardData.put("totalUsers", 150);
            dashboardData.put("activeBids", 45);
            dashboardData.put("completedAuctions", 89);
            dashboardData.put("revenue", 12500.75);
            dashboardData.put("recentActivity", new String[]{
                    "User john_doe placed a bid on Item #123",
                    "User jane_smith won auction for Item #456",
                    "New user alice_wonder registered"
            });

            return ResponseEntity.ok(ApiResponse.success("Dashboard data retrieved successfully", dashboardData));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error retrieving dashboard data: " + e.getMessage()));
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalAuctions", 234);
            stats.put("successfulAuctions", 189);
            stats.put("averageBidAmount", 245.50);
            stats.put("userEngagementRate", 78.5);

            return ResponseEntity.ok(ApiResponse.success("Statistics retrieved successfully", stats));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error retrieving statistics: " + e.getMessage()));
        }
    }
}