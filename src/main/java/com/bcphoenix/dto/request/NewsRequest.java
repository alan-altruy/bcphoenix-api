package com.bcphoenix.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewsRequest(

    @NotBlank
    @Size(max = 200)
    String title,

    @NotBlank
    @Size(max = 500)
    String summary,

    @NotBlank
    @Size(max = 10000)
    String content,

    @Size(max = 500)
    String coverImage,

    boolean published

) {}