package com.ridesharing.controller;

import com.ridesharing.model.User;
import com.ridesharing.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        String email = authentication.getName();
        var user = userService.getUserProfile(email);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.badRequest().body("User not found");
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody User userDetails, Authentication authentication) {
        String email = authentication.getName();
        var userOpt = userService.getUserProfile(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            User updatedUser = userService.updateUserProfile(user.getId(), userDetails);
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.badRequest().body("User not found");
    }
}