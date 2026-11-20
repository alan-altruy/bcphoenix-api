package com.bcphoenix.dto.response.visitor;

import java.util.List;

public record AffiliateSeasonStatsResponse(
        AffiliateResponse affiliate,
        List<GameScoreResponse> games,
        int numberOfGames,
        int totalScratch,
        int averageScratch,
        int handicap,
        int totalHandicap,
        int totalWithHandicap,
        int averageWithHandicap
) {
}