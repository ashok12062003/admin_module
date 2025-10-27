package com.ridesharing.security;

import com.ridesharing.model.Admin;
import com.ridesharing.model.Driver;
import com.ridesharing.model.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@Builder
public class UserPrincipal implements UserDetails {
    
    // Constants for repeated literals
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

    // Static factory method for Admin
    public static UserPrincipal createAdmin(Admin admin) {
        List<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority(ROLE_PREFIX + admin.getRole())
        );

        return UserPrincipal.builder()
                .id(admin.getId())
                .username(admin.getUsername())
                .password(admin.getPassword())
                .role(admin.getRole())
                .authorities(authorities)
                .active(true)
                .build();
    }

    // Static factory method for Driver
    public static UserPrincipal createDriver(Driver driver) {
        List<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority(ROLE_PREFIX + driver.getRole())
        );

        return UserPrincipal.builder()
                .id(driver.getId())
                .username(driver.getEmail())
                .email(driver.getEmail())
                .password(driver.getPassword())
                .firstName(driver.getFirstName())
                .lastName(driver.getLastName())
                .phone(driver.getPhone())
                .licenseNumber(driver.getLicenseNumber())
                .vehicleType(driver.getVehicleType())
                .vehicleNumber(driver.getVehicleNumber())
                .role(driver.getRole())
                .approved(driver.isApproved())
                .active(driver.isActive())
                .authorities(authorities)
                .build();
    }

    // Static factory method for User
    public static UserPrincipal createUser(User user) {
        List<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority(ROLE_PREFIX + user.getRole())
        );

        return UserPrincipal.builder()
                .id(user.getId())
                .username(user.getEmail())
                .email(user.getEmail())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .role(user.getRole())
                .active(user.isActive())
                .authorities(authorities)
                .build();
    }

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