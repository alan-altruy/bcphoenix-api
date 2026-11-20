package com.bcphoenix.service;

import com.bcphoenix.dto.response.visitor.AffiliateSeasonStatsResponse;
import com.bcphoenix.dto.response.visitor.AffiliateSessionStatsResponse;
import com.bcphoenix.dto.response.visitor.AffiliateResponse;
import com.bcphoenix.dto.response.visitor.GameScoreResponse;
import com.bcphoenix.dto.response.visitor.SessionScoresResponse;

import com.bcphoenix.model.*;
import com.bcphoenix.repository.GameRepository;
import com.bcphoenix.repository.SeasonRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private static final int REFERENCE_AVERAGE = 200;
    private static final double HANDICAP_FACTOR = 0.80;

    private final GameRepository gameRepository;
    private final GameService gameService;
    private final SeasonRepository seasonRepository;

    @Transactional(readOnly = true)
    public List<AffiliateSeasonStatsResponse> getSeasonStatistics(Integer seasonId, Integer dayOfWeek) {

        Season season = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new RuntimeException("Saison introuvable"));

        List<Game> games = gameRepository.findSeasonGames(
                EventType.LEAGUE,
                season.getStartDate(),
                season.getEndDate(),
                dayOfWeek
        );

        // Regroupement des parties par affilié
        Map<Integer, List<Game>> gamesByAffiliate = new LinkedHashMap<>();

        for (Game game : games) {
            Integer affiliateId = game.getAffiliate().getId();

            gamesByAffiliate
                    .computeIfAbsent(affiliateId, key -> new java.util.ArrayList<>())
                    .add(game);
        }

        return gamesByAffiliate.values().stream()
                .map(this::calculateAffiliateStatistics)
                .toList();
    }

    private AffiliateSeasonStatsResponse calculateAffiliateStatistics(List<Game> games) {

        Affiliate affiliate = games.getFirst().getAffiliate();

        int numberOfGames = games.size();

        int totalScratch = games.stream()
                .mapToInt(Game::getScore)
                .sum();

        // Moyenne entière, comme dans les anciennes statistiques
        int averageScratch = totalScratch / numberOfGames;

        int handicap = calculateHandicap(averageScratch);

        int totalHandicap = handicap * numberOfGames;

        int totalWithHandicap = totalScratch + totalHandicap;

        int averageWithHandicap = averageScratch + handicap;

        return new AffiliateSeasonStatsResponse(
                AffiliateResponse.from(affiliate),
                games.stream().map(GameScoreResponse::from).toList(),
                numberOfGames,
                totalScratch,
                averageScratch,
                handicap,
                totalHandicap,
                totalWithHandicap,
                averageWithHandicap
        );
    }

    private int calculateHandicap(int average) {
        return (int) Math.ceil(
                Math.max(0, (REFERENCE_AVERAGE - average) * HANDICAP_FACTOR)
        );
    }

    @Transactional(readOnly = true)
    public SessionScoresResponse getSessionStatistics(LocalDate date, Integer dayOfWeek) {

        Season season = seasonRepository.findAll().stream()
                .filter(s -> !date.isBefore(s.getStartDate())
                        && !date.isAfter(s.getEndDate()))
                .max(Comparator.comparing(Season::getStartDate))
                .orElseThrow(() -> new RuntimeException(
                        "Aucune saison trouvée pour cette date"
                ));

        // Parties de la session du jour
        List<Game> sessionGames = gameRepository
                .findByEvent_EventDateAndEvent_EventTypeOrderByAffiliate_LastNameAscAffiliate_FirstNameAscGameNumberAsc(
                        date,
                        EventType.LEAGUE
                );

        if (sessionGames.isEmpty()) {
            throw new RuntimeException("Aucune partie trouvée pour cette session");
        }

        // Historique de la saison jusqu'à cette date
        List<Game> seasonGames = gameRepository.findSeasonGames(
                EventType.LEAGUE,
                season.getStartDate(),
                date,
                dayOfWeek
        );

        // Exclusion des parties de la session actuelle
        Set<Integer> sessionGameIds = sessionGames.stream()
                .map(Game::getId)
                .collect(Collectors.toSet());

        seasonGames = seasonGames.stream()
                .filter(game -> !sessionGameIds.contains(game.getId()))
                .toList();

        // Regroupement de l'historique par affilié
        Map<Integer, List<Game>> seasonGamesByAffiliate = seasonGames.stream()
                .collect(Collectors.groupingBy(
                        game -> game.getAffiliate().getId(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        // Regroupement des parties du jour par affilié
        Map<Integer, List<Game>> sessionGamesByAffiliate = sessionGames.stream()
                .collect(Collectors.groupingBy(
                        game -> game.getAffiliate().getId(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        List<AffiliateSessionStatsResponse> players =
                sessionGamesByAffiliate.values().stream()
                        .map(dailyGames -> {

                            Affiliate affiliate = dailyGames.getFirst().getAffiliate();

                            Integer affiliateId = affiliate.getId();

                            List<Game> affiliateSeasonGames =
                                    seasonGamesByAffiliate.getOrDefault(
                                            affiliateId,
                                            List.of()
                                    );

                            // Calcul du handicap sur les parties précédentes
                            int totalPreviousScratch = affiliateSeasonGames.stream()
                                    .mapToInt(Game::getScore)
                                    .sum();

                            int numberOfPreviousGames = affiliateSeasonGames.size();

                            int handicap = numberOfPreviousGames == 0
                                    ? calculateHandicap(0)
                                    : calculateHandicap(
                                            totalPreviousScratch / numberOfPreviousGames
                                    );

                            // Total des trois parties de la session
                            int totalScratch = dailyGames.stream()
                                    .mapToInt(Game::getScore)
                                    .sum();

                            int totalWithHandicap =
                                    totalScratch + handicap * dailyGames.size();

                            return new AffiliateSessionStatsResponse(
                                    AffiliateResponse.from(affiliate),
                                    dailyGames.stream()
                                            .map(GameScoreResponse::from)
                                            .toList(),
                                    totalScratch,
                                    handicap,
                                    totalWithHandicap
                            );
                        })
                        .toList();

        return new SessionScoresResponse(
                season,
                date,
                players
        );
    }

    public SessionScoresResponse getLastSessionStatistics(int dayOfWeek) {

        List<Game> lastGames = gameService.findLastLeagueGamesByDay(dayOfWeek);

        if (lastGames.isEmpty()) {
            throw new RuntimeException("Aucune session trouvée");
        }

        Game lastGame = lastGames.getFirst();

        LocalDate date = lastGame.getEvent().getEventDate();

        return getSessionStatistics(date, dayOfWeek);
    }
}