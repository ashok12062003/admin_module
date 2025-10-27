package com.ridesharing.controller;

import com.ridesharing.model.User;
import com.ridesharing.model.Driver;
import com.ridesharing.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/admin/login")
    public ResponseEntity<?> adminLogin(@RequestBody Map<String, String> request) {
        Map<String, Object> result = authService.adminLogin(
            request.get("username"), 
            request.get("password")
        );
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.badRequest().body(Map.of("error", "Invalid credentials"));
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> userLogin(@RequestBody Map<String, String> request) {
        Map<String, Object> result = authService.userLogin(
            request.get("email"), 
            request.get("password")
        );
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.badRequest().body(Map.of("error", "Invalid credentials"));
    }

    @PostMapping("/driver/login")
    public ResponseEntity<?> driverLogin(@RequestBody Map<String, String> request) {
        Map<String, Object> result = authService.driverLogin(
            request.get("email"), 
            request.get("password")
        );
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.badRequest().body(Map.of("error", "Invalid credentials or driver not approved"));
    }

    @PostMapping("/user/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        Map<String, Object> result = authService.registerUser(user);
        if (result.containsKey("error")) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/driver/register")
    public ResponseEntity<?> registerDriver(@RequestBody Driver driver) {
        Map<String, Object> result = authService.registerDriver(driver);
        if (result.containsKey("error")) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }
}