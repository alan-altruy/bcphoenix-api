package com.bcphoenix.dto.response;

import java.time.LocalDateTime;

public record NewsSummaryResponse(
    Long id,
    String title,
    String slug,
    String summary,
    String coverImage,
    LocalDateTime publishedAt
) {}