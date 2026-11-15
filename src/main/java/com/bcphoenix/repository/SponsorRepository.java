package com.bcphoenix.repository;

import com.bcphoenix.model.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SponsorRepository extends JpaRepository<Sponsor, Integer> {

    boolean existsByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCaseAndIdNot(String name, Integer id);
    boolean existsByImage_Id(Integer imageId);
}