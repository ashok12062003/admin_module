package com.ridesharing.repository;

import com.ridesharing.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Driver> findByApprovedTrueAndActiveTrue();
    List<Driver> findByApprovedFalse();
    
    @Query("SELECT COUNT(d) FROM Driver d WHERE d.approved = true AND d.active = true")
    Long countByApprovedTrueAndActiveTrue();
    
    @Query("SELECT COUNT(d) FROM Driver d WHERE d.approved = false")
    Long countByApprovedFalse();
}