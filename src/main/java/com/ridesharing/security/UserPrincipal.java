package com.ridesharing.security;

import com.ridesharing.model.Admin;
import com.ridesharing.model.Driver;
import com.ridesharing.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserPrincipal implements UserDetails {
    
    private static final String ROLE_PREFIX = "ROLE_";
    
    private Long id;
    private String username;
    private String email;
    private String password;
    private String role;
    private String firstName;
    private String lastName;
    private String phone;
    private String licenseNumber;
    private String vehicleType;
    private String vehicleNumber;
    private boolean approved;
    private boolean active;
    private Collection<? extends GrantedAuthority> authorities;

    // Constructor
    public UserPrincipal(Long id, String username, String email, String password, String role, 
                        String firstName, String lastName, String phone, String licenseNumber,
                        String vehicleType, String vehicleNumber, boolean approved, boolean active,
                        Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.licenseNumber = licenseNumber;
        this.vehicleType = vehicleType;
        this.vehicleNumber = vehicleNumber;
        this.approved = approved;
        this.active = active;
        this.authorities = authorities;
    }

    // Static factory method for Admin
    public static UserPrincipal createAdmin(Admin admin) {
        List<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority(ROLE_PREFIX + admin.getRole())
        );

        return new UserPrincipal(
            admin.getId(),
            admin.getUsername(),
            null,
            admin.getPassword(),
            admin.getRole(),
            null,
            null,
            null,
            null,
            null,
            null,
            true,
            true,
            authorities
        );
    }

    // Static factory method for Driver
    public static UserPrincipal createDriver(Driver driver) {
        List<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority(ROLE_PREFIX + driver.getRole())
        );

        return new UserPrincipal(
            driver.getId(),
            driver.getEmail(),
            driver.getEmail(),
            driver.getPassword(),
            driver.getRole(),
            driver.getFirstName(),
            driver.getLastName(),
            driver.getPhone(),
            driver.getLicenseNumber(),
            driver.getVehicleType(),
            driver.getVehicleNumber(),
            driver.isApproved(),
            driver.isActive(),
            authorities
        );
    }

    // Static factory method for User
    public static UserPrincipal createUser(User user) {
        List<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority(ROLE_PREFIX + user.getRole())
        );

        return new UserPrincipal(
            user.getId(),
            user.getEmail(),
            user.getEmail(),
            user.getPassword(),
            user.getRole(),
            user.getFirstName(),
            user.getLastName(),
            user.getPhone(),
            null,
            null,
            null,
            user.isActive(),
            user.isActive(),
            authorities
        );
    }

    // Getters
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhone() { return phone; }
    public String getLicenseNumber() { return licenseNumber; }
    public String getVehicleType() { return vehicleType; }
    public String getVehicleNumber() { return vehicleNumber; }
    public boolean isApproved() { return approved; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}