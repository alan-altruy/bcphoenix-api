package com.bcphoenix.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AffiliateRequest(
        @NotBlank @Size(max=100) String lastName,
        @NotBlank @Size(max=50) String firstName,
        @NotBlank @Size(max=30) String licenseNumber,
        LocalDate birthDate,
        Boolean active,
        Integer userId
) {
}
