package com.ridesharing.service;

import com.ridesharing.model.Admin;
import com.ridesharing.model.User;
import com.ridesharing.model.Driver;
import com.ridesharing.repository.AdminRepository;
import com.ridesharing.repository.UserRepository;
import com.ridesharing.repository.DriverRepository;
import com.ridesharing.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    // Constants for repeated string literals
    private static final String KEY_TOKEN = "token";
    private static final String KEY_ERROR = "error";
    private static final String KEY_USER = "user";
    private static final String KEY_DRIVER = "driver";
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_ROLE = "role";
    private static final String KEY_ACTIVE = "active";
    private static final String KEY_PERMISSIONS = "permissions";
    private static final String KEY_LICENSE_NUMBER = "licenseNumber";
    private static final String KEY_VEHICLE_TYPE = "vehicleType";
    private static final String KEY_VEHICLE_NUMBER = "vehicleNumber";
    private static final String KEY_APPROVED = "approved";
    private static final String KEY_MESSAGE = "message";

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public Map<String, Object> adminLogin(String username, String password) {
        Admin admin = adminRepository.findByUsername(username).orElse(null);
        
        if (admin != null && passwordEncoder.matches(password, admin.getPassword())) {
            UserPrincipal userPrincipal = UserPrincipal.createAdmin(admin);
            
            String token = jwtService.generateToken(userPrincipal);
            
            Map<String, Object> response = new HashMap<>();
            response.put(KEY_TOKEN, token);
            response.put(KEY_USER, Map.of(
                KEY_ID, admin.getId(),
                KEY_USERNAME, admin.getUsername(),
                KEY_ROLE, admin.getRole(),
                KEY_PERMISSIONS, admin.getPermissions() != null ? admin.getPermissions() : ""
            ));
            return response;
        }
        return Map.of(KEY_ERROR, "Invalid username or password");
    }

    @Transactional
    public Map<String, Object> userLogin(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null && passwordEncoder.matches(password, user.getPassword()) && user.isActive()) {
            UserPrincipal userPrincipal = UserPrincipal.createUser(user);
            
            String token = jwtService.generateToken(userPrincipal);
            
            Map<String, Object> response = new HashMap<>();
            response.put(KEY_TOKEN, token);
            response.put(KEY_USER, Map.of(
                KEY_ID, user.getId(),
                KEY_EMAIL, user.getEmail(),
                KEY_FIRST_NAME, user.getFirstName(),
                KEY_LAST_NAME, user.getLastName(),
                KEY_PHONE, user.getPhone(),
                KEY_ROLE, user.getRole(),
                KEY_ACTIVE, user.isActive()
            ));
            return response;
        }
        return Map.of(KEY_ERROR, "Invalid email, password, or account not active");
    }

    @Transactional
    public Map<String, Object> driverLogin(String email, String password) {
        Driver driver = driverRepository.findByEmail(email).orElse(null);
        if (driver != null && passwordEncoder.matches(password, driver.getPassword()) && driver.isApproved() && driver.isActive()) {
            UserPrincipal userPrincipal = UserPrincipal.createDriver(driver);
            
            String token = jwtService.generateToken(userPrincipal);
            
            Map<String, Object> response = new HashMap<>();
            response.put(KEY_TOKEN, token);
            
            Map<String, Object> userDetails = new HashMap<>();
            userDetails.put(KEY_ID, driver.getId());
            userDetails.put(KEY_EMAIL, driver.getEmail());
            userDetails.put(KEY_FIRST_NAME, driver.getFirstName());
            userDetails.put(KEY_LAST_NAME, driver.getLastName());
            userDetails.put(KEY_PHONE, driver.getPhone());
            userDetails.put(KEY_LICENSE_NUMBER, driver.getLicenseNumber());
            userDetails.put(KEY_VEHICLE_TYPE, driver.getVehicleType());
            userDetails.put(KEY_VEHICLE_NUMBER, driver.getVehicleNumber());
            userDetails.put(KEY_ROLE, driver.getRole());
            userDetails.put(KEY_APPROVED, driver.isApproved());
            userDetails.put(KEY_ACTIVE, driver.isActive());
            
            response.put(KEY_USER, userDetails);
            return response;
        }
        
        if (driver == null) {
            return Map.of(KEY_ERROR, "Driver not found");
        } else if (!driver.isApproved()) {
            return Map.of(KEY_ERROR, "Driver account not approved yet");
        } else if (!driver.isActive()) {
            return Map.of(KEY_ERROR, "Driver account is deactivated");
        } else {
            return Map.of(KEY_ERROR, "Invalid email or password");
        }
    }

    @Transactional
    public Map<String, Object> registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return Map.of(KEY_ERROR, "Email already exists");
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole("USER");
        }
        user.setActive(true);
        
        User savedUser = userRepository.save(user);
        
        UserPrincipal userPrincipal = UserPrincipal.createUser(savedUser);
        String token = jwtService.generateToken(userPrincipal);
        
        Map<String, Object> response = new HashMap<>();
        response.put(KEY_TOKEN, token);
        response.put(KEY_USER, Map.of(
            KEY_ID, savedUser.getId(),
            KEY_EMAIL, savedUser.getEmail(),
            KEY_FIRST_NAME, savedUser.getFirstName(),
            KEY_LAST_NAME, savedUser.getLastName(),
            KEY_PHONE, savedUser.getPhone(),
            KEY_ROLE, savedUser.getRole(),
            KEY_ACTIVE, savedUser.isActive()
        ));
        return response;
    }

    @Transactional
    public Map<String, Object> registerDriver(Driver driver) {
        if (driverRepository.existsByEmail(driver.getEmail())) {
            return Map.of(KEY_ERROR, "Email already exists");
        }
        
        driver.setPassword(passwordEncoder.encode(driver.getPassword()));
        if (driver.getRole() == null) {
            driver.setRole("DRIVER");
        }
        driver.setApproved(false);
        driver.setActive(true);
        
        Driver savedDriver = driverRepository.save(driver);
        
        Map<String, Object> response = new HashMap<>();
        response.put(KEY_MESSAGE, "Driver registration submitted for approval");
        
        Map<String, Object> driverDetails = new HashMap<>();
        driverDetails.put(KEY_ID, savedDriver.getId());
        driverDetails.put(KEY_EMAIL, savedDriver.getEmail());
        driverDetails.put(KEY_FIRST_NAME, savedDriver.getFirstName());
        driverDetails.put(KEY_LAST_NAME, savedDriver.getLastName());
        driverDetails.put(KEY_PHONE, savedDriver.getPhone());
        driverDetails.put(KEY_LICENSE_NUMBER, savedDriver.getLicenseNumber());
        driverDetails.put(KEY_VEHICLE_TYPE, savedDriver.getVehicleType());
        driverDetails.put(KEY_VEHICLE_NUMBER, savedDriver.getVehicleNumber());
        driverDetails.put(KEY_APPROVED, savedDriver.isApproved());
        driverDetails.put(KEY_ACTIVE, savedDriver.isActive());
        
        response.put(KEY_DRIVER, driverDetails);
        return response;
    }

    @Transactional
    public Map<String, Object> createAdmin(Admin admin) {
        if (adminRepository.existsByUsername(admin.getUsername())) {
            return Map.of(KEY_ERROR, "Username already exists");
        }
        
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        if (admin.getRole() == null) {
            admin.setRole("ADMIN");
        }
        
        Admin savedAdmin = adminRepository.save(admin);
        
        return Map.of(
            KEY_MESSAGE, "Admin created successfully",
            "admin", Map.of(
                KEY_ID, savedAdmin.getId(),
                KEY_USERNAME, savedAdmin.getUsername(),
                KEY_ROLE, savedAdmin.getRole(),
                KEY_PERMISSIONS, savedAdmin.getPermissions() != null ? savedAdmin.getPermissions() : ""
            )
        );
    }
}