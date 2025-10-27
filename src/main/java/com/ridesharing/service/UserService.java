package com.ridesharing.service;

import com.ridesharing.model.User;
import com.ridesharing.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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