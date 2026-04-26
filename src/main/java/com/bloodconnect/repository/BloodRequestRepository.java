package com.bloodconnect.repository;

import com.bloodconnect.entity.BloodRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BloodRequestRepository extends JpaRepository<BloodRequest, Long> {
    List<BloodRequest> findByBloodGroupAndCityAndStatus(String bloodGroup, String city, String status);
    List<BloodRequest> findByStatus(String status);
}
