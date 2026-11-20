package com.bcphoenix.repository;

import com.bcphoenix.model.EventType;
import com.bcphoenix.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

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
    List<Game> findByEvent_EventDateAndEvent_EventTypeOrderByAffiliate_LastNameAscAffiliate_FirstNameAscGameNumberAsc(
        LocalDate eventDate,
        EventType eventType
    );
    @Query("""
        SELECT g FROM Game g
        WHERE g.event.eventType = com.bcphoenix.model.EventType.LEAGUE
        AND FUNCTION('DAYOFWEEK', g.event.eventDate) = :dayOfWeek
        ORDER BY g.event.eventDate DESC, g.event.id DESC
        """)
    List<Game> findLastLeagueGameByDay(
        Integer dayOfWeek,
        Pageable pageable
    );

    @Query("""
        SELECT g FROM Game g
        WHERE g.event.eventType = :eventType
        AND g.event.eventDate BETWEEN :startDate AND :endDate
        AND FUNCTION('DAYOFWEEK', g.event.eventDate) = :dayOfWeek
        ORDER BY g.affiliate.lastName,
                g.affiliate.firstName,
                g.event.eventDate,
                g.gameNumber
        """)
    List<Game> findSeasonGames(
            @Param("eventType") EventType eventType,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("dayOfWeek") Integer dayOfWeek
    );
}
