package com.bcphoenix.dto.response;

import java.time.LocalDateTime;

public record NewsResponse(
    Long id,
    String title,
    String slug,
    String summary,
    String content,
    String coverImage,
    boolean published,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime publishedAt
) {}