package com.bcphoenix.repository;

import com.bcphoenix.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game,Integer> {
    boolean existsByAffiliateIdAndEventIdAndGameNumber(Integer affiliateId, Integer eventId, Integer gameNumber);
    boolean existsByAffiliateIdAndEventIdAndGameNumberAndIdNot(Integer affiliateId, Integer eventId, Integer gameNumber, Integer id);
    boolean existsByAffiliateId(Integer affiliateId);
    boolean existsByEventId(Integer eventId);
}
