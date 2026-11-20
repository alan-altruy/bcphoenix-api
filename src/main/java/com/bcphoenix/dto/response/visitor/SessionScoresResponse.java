package com.bcphoenix.dto.response.visitor;

import com.bcphoenix.model.Season;
import java.time.LocalDate;
import java.util.List;

public record SessionScoresResponse(
        Season season,
        LocalDate date,
        List<AffiliateSessionStatsResponse> affiliates
) {}