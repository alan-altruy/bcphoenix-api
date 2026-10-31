package com.bcphoenix.security;


import com.bcphoenix.dto.response.ApiError;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class SecurityErrorWriter {
    private final JsonMapper jsonMapper;

    public SecurityErrorWriter(JsonMapper jsonMapper){
        this.jsonMapper=jsonMapper;
    }

    public void write(HttpServletResponse response, int status, String message) throws
            IOException{
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        jsonMapper.writeValue(response.getWriter(), new ApiError(status, message, null));
    }
}
