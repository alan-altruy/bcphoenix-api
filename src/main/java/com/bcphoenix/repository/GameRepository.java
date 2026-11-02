package com.bcphoenix.repository;

import com.bcphoenix.model.EventType;
import com.bcphoenix.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game,Integer> {
    boolean existsByAffiliateIdAndEventIdAndGameNumber(Integer affiliateId, Integer eventId, Integer gameNumber);
    boolean existsByAffiliateIdAndEventIdAndGameNumberAndIdNot(Integer affiliateId, Integer eventId, Integer gameNumber, Integer id);
    boolean existsByAffiliateId(Integer affiliateId);
    boolean existsByEventId(Integer eventId);
    Optional<Game> findFirstByEvent_EventTypeOrderByEvent_EventDateDescEvent_IdDesc(EventType eventType);
    List<Game> findByEventIdOrderByAffiliate_LastNameAscGameNumberAsc(Integer eventId);
    Optional<Game> findFirstByEvent_EventTypeAndEvent_EventDateOrderByEvent_IdDesc(EventType eventType, LocalDate eventDate);
}
