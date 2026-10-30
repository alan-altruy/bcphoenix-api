package com.bcphoenix.dto.response;
import java.util.Map;

public record ApiError(
        int status,
        String message,
        Map<String, String> errors ) {
}

