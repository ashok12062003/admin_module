package com.ridesharing.repository;

import com.ridesharing.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Driver> findByApprovedFalse();
    List<Driver> findByApprovedTrueAndActiveTrue();
    long countByApprovedTrueAndActiveTrue();
    long countByApprovedFalse();
}