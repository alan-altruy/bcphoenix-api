package com.bcphoenix.repository;

import com.bcphoenix.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository  extends JpaRepository<Event,Integer> {
    boolean existsByLocationId(Integer id);
}
