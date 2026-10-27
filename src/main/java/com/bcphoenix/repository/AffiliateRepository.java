package com.bcphoenix.repository;

import com.bcphoenix.model.Affiliate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AffiliateRepository extends JpaRepository<Affiliate,Integer> {
    boolean existsByLicenseNumberIgnoreCaseAndIdNot(String licenseNumber,Integer id);
    boolean existsByLicenseNumberIgnoreCase(String licenseNumber);
    boolean existsByUserId(Integer id);
    boolean existsByUserIdAndIdNot(Integer userId, Integer id);
}
