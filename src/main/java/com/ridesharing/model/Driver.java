package com.ridesharing.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "drivers")
@Data
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    private String phone;
    private String licenseNumber;
    private String vehicleType;
    private String vehicleNumber;
    private String role = "DRIVER";
    private boolean approved = false;
    private boolean active = true;
    
    // Add any additional fields you need
}