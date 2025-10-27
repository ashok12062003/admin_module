package com.ridesharing.model;
import com.ridesharing.model.Ride;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "rides")
@Data
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Driver driver;
    
    @Column(nullable = false)
    private String pickupLocation;
    
    @Column(nullable = false)
    private String dropoffLocation;
    
    private Double distance;
    private Double fare;
    
    @Column(nullable = false)
    private String status = "REQUESTED"; // REQUESTED, ACCEPTED, IN_PROGRESS, COMPLETED, CANCELLED
    
    private LocalDateTime requestTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}