package com.bcphoenix.dto.response.visitor;

import java.util.List;

public record AffiliateSessionStatsResponse(
        AffiliateResponse affiliate,
        List<GameScoreResponse> games,
        int totalScratch,
        int handicap,
        int totalWithHandicap
) {}