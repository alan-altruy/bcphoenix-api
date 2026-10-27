package com.bcphoenix.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LocationRequest(
        @NotBlank @Size(max = 100) String name,
        @Size(max = 100) String city,
        @Size(max = 255) String address
) {
}
