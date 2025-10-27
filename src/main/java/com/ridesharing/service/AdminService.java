package com.ridesharing.service;

import com.ridesharing.model.User;
import com.ridesharing.model.Driver;
import com.ridesharing.repository.UserRepository;
import com.ridesharing.repository.DriverRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final DriverRepository driverRepository;

    public AdminService(UserRepository userRepository, DriverRepository driverRepository) {
        this.userRepository = userRepository;
        this.driverRepository = driverRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findByActiveTrue();
    }

    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    public List<Driver> getPendingDrivers() {
        return driverRepository.findByApprovedFalse();
    }

    public List<Driver> getApprovedDrivers() {
        return driverRepository.findByApprovedTrueAndActiveTrue();
    }

    public Driver approveDriver(Long driverId) {
        Driver driver = driverRepository.findById(driverId).orElse(null);
        if (driver != null) {
            driver.setApproved(true);
            return driverRepository.save(driver);
        }
        return null;
    }

    public boolean deactivateUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setActive(false);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean activateUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setActive(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean deactivateDriver(Long driverId) {
        Driver driver = driverRepository.findById(driverId).orElse(null);
        if (driver != null) {
            driver.setActive(false);
            driverRepository.save(driver);
            return true;
        }
        return false;
    }

    public boolean activateDriver(Long driverId) {
        Driver driver = driverRepository.findById(driverId).orElse(null);
        if (driver != null) {
            driver.setActive(true);
            driverRepository.save(driver);
            return true;
        }
        return false;
    }

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.countByActiveTrue());
        stats.put("totalDrivers", driverRepository.countByApprovedTrueAndActiveTrue());
        stats.put("pendingDrivers", driverRepository.countByApprovedFalse());
        stats.put("totalRides", 0L); // You can add ride repository later
        return stats;
    }
}