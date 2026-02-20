package com.microservices.tnb.bffservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;

/**
 * Manages JWT tokens stored in the reactive WebSession.
 * Tokens are NEVER exposed to the client — only the session ID (HttpOnly cookie) is sent.
 */
@Service
public class TokenSessionService {

    private static final String ACCESS_TOKEN_KEY = "access_token";
    private static final String REFRESH_TOKEN_KEY = "refresh_token";
    private static final String ACCESS_TOKEN_EXPIRES_AT_KEY = "access_token_expires_at";
    private static final String USERNAME_KEY = "username";

    public Mono<Void> storeTokens(WebSession session, String accessToken, String refreshToken,
                                   long expiresInSeconds, String username) {
        session.getAttributes().put(ACCESS_TOKEN_KEY, accessToken);
        session.getAttributes().put(REFRESH_TOKEN_KEY, refreshToken);
        session.getAttributes().put(ACCESS_TOKEN_EXPIRES_AT_KEY,
                Instant.now().plusSeconds(expiresInSeconds).toEpochMilli());
        session.getAttributes().put(USERNAME_KEY, username);
        return Mono.empty();
    }

    public String getAccessToken(WebSession session) {
        return (String) session.getAttributes().get(ACCESS_TOKEN_KEY);
    }

    public String getRefreshToken(WebSession session) {
        return (String) session.getAttributes().get(REFRESH_TOKEN_KEY);
    }

    public String getUsername(WebSession session) {
        return (String) session.getAttributes().get(USERNAME_KEY);
    }

    public boolean isAccessTokenExpired(WebSession session) {
        Long expiresAt = (Long) session.getAttributes().get(ACCESS_TOKEN_EXPIRES_AT_KEY);
        if (expiresAt == null) {
            return true;
        }
        // Consider expired if less than 60 seconds remaining
        return Instant.now().toEpochMilli() >= (expiresAt - 60_000);
    }

    public boolean hasTokens(WebSession session) {
        return session.getAttributes().containsKey(ACCESS_TOKEN_KEY)
                && session.getAttributes().containsKey(REFRESH_TOKEN_KEY);
    }

    public Mono<Void> clearTokens(WebSession session) {
        return session.invalidate();
    }
}
