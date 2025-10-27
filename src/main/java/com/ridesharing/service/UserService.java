package com.ridesharing.service;

import com.ridesharing.model.User;
import com.ridesharing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> getUserProfile(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateUserProfile(Long userId, User userDetails) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setFirstName(userDetails.getFirstName());
            user.setLastName(userDetails.getLastName());
            user.setPhone(userDetails.getPhone());
            return userRepository.save(user);
        }
        return null;
    }
}