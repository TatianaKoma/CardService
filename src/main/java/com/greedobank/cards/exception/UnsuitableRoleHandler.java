package com.greedobank.cards.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.greedobank.cards.utils.ResponseMessages.NO_ACCESS;

@Component
@RequiredArgsConstructor
public class UnsuitableRoleHandler implements AccessDeniedHandler {
    private final ObjectMapper mapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        JsonErrorResponse accessDeniedResponse =
                new JsonErrorResponse(NO_ACCESS.getDescription());
        mapper.writeValue(response.getOutputStream(), accessDeniedResponse);
    }
}
