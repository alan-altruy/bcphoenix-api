package com.bcphoenix.dto;

import com.bcphoenix.model.EventType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record EventRequest(
        @NotNull LocalDate eventDate,
        @NotNull EventType eventType,
        @NotNull Integer locationId,
        @Size(max=255) String notes
        ) {
}
