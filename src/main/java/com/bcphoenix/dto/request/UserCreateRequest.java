package com.bcphoenix.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @NotBlank @Size(max = 100) String lastName,
        @NotBlank @Size(max = 50) String firstName,
        @NotNull Integer roleId,
        @NotBlank @Email @Size(max = 100) String email,
        @NotBlank @Size(min = 12, max = 64) String password
) {
}
