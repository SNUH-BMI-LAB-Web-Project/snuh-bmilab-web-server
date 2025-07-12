package com.bmilab.backend.global.jwt;

import com.bmilab.backend.global.exception.ErrorCode;
import com.bmilab.backend.global.exception.ErrorResponse;
import com.bmilab.backend.global.exception.GlobalErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String BEARER_AUTH = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        String accessToken = getAccessToken(authorizationHeader);

        if (response.isCommitted()) return;

        if (!tokenProvider.validToken(accessToken)) {
            sendUnauthorizedResponse(response);
            return;
        }

        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_AUTH)) {
            return authorizationHeader.substring(BEARER_AUTH.length());
        }

        return null;
    }

    private void sendUnauthorizedResponse(HttpServletResponse response) throws IOException {
        ErrorCode errorCode = GlobalErrorCode.INVALID_ACCESS_TOKEN;
        response.addHeader("Content-Type", "application/json; charset=UTF-8");
        response.setStatus(errorCode.getHttpStatus().value());
        response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse.from(errorCode, Instant.now())));
        response.getWriter().flush();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludes = {"/auth/"};
        String path = request.getRequestURI();

        return Arrays.stream(excludes).anyMatch(path::startsWith);
    }
}
