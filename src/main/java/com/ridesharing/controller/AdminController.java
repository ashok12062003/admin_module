package com.ridesharing.controller;

import com.ridesharing.model.Admin;
import com.ridesharing.service.AdminService;
import com.ridesharing.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final AuthService authService;

    @GetMapping("/dashboard/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/drivers")
    public ResponseEntity<?> getAllDrivers() {
        return ResponseEntity.ok(adminService.getAllDrivers());
    }

    @GetMapping("/drivers/pending")
    public ResponseEntity<?> getPendingDrivers() {
        return ResponseEntity.ok(adminService.getPendingDrivers());
    }

    @GetMapping("/drivers/approved")
    public ResponseEntity<?> getApprovedDrivers() {
        return ResponseEntity.ok(adminService.getApprovedDrivers());
    }

    @PutMapping("/drivers/{driverId}/approve")
    public ResponseEntity<?> approveDriver(@PathVariable Long driverId) {
        var driver = adminService.approveDriver(driverId);
        if (driver != null) {
            return ResponseEntity.ok(Map.of("message", "Driver approved successfully"));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "Driver not found"));
    }

    @PutMapping("/users/{userId}/deactivate")
    public ResponseEntity<?> deactivateUser(@PathVariable Long userId) {
        if (adminService.deactivateUser(userId)) {
            return ResponseEntity.ok(Map.of("message", "User deactivated successfully"));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
    }

    @PutMapping("/users/{userId}/activate")
    public ResponseEntity<?> activateUser(@PathVariable Long userId) {
        if (adminService.activateUser(userId)) {
            return ResponseEntity.ok(Map.of("message", "User activated successfully"));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
    }

    @PutMapping("/drivers/{driverId}/deactivate")
    public ResponseEntity<?> deactivateDriver(@PathVariable Long driverId) {
        if (adminService.deactivateDriver(driverId)) {
            return ResponseEntity.ok(Map.of("message", "Driver deactivated successfully"));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "Driver not found"));
    }

    @PutMapping("/drivers/{driverId}/activate")
    public ResponseEntity<?> activateDriver(@PathVariable Long driverId) {
        if (adminService.activateDriver(driverId)) {
            return ResponseEntity.ok(Map.of("message", "Driver activated successfully"));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "Driver not found"));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAdmin(@RequestBody Admin admin) {
        Map<String, Object> result = authService.createAdmin(admin);
        if (result.containsKey("error")) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }
}