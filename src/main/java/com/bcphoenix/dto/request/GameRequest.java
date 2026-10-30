package com.bcphoenix.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record GameRequest(
        @NotNull @Min(0) @Max(300) Integer score,
        @NotNull @Min(1) Integer gameNumber,
        @NotNull Integer eventId,
        @NotNull Integer affiliateId
        ) {
}
