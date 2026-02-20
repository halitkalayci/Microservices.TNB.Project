package com.microservices.tnb.bffservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

/**
 * Relays requests to the Gateway with JWT Bearer token from the session.
 * Automatically refreshes access tokens when expired.
 */
@Service
public class GatewayRelayService {

    private static final Logger log = LoggerFactory.getLogger(GatewayRelayService.class);

    private final WebClient gatewayWebClient;
    private final TokenSessionService tokenSessionService;
    private final AuthService authService;

    public GatewayRelayService(@Qualifier("gatewayWebClient") WebClient gatewayWebClient,
                                TokenSessionService tokenSessionService,
                                AuthService authService) {
        this.gatewayWebClient = gatewayWebClient;
        this.tokenSessionService = tokenSessionService;
        this.authService = authService;
    }

    /**
     * Performs a GET request to the gateway, attaching JWT from session.
     */
    public Mono<String> relayGet(String path, WebSession session) {
        return ensureValidToken(session)
                .flatMap(accessToken -> gatewayWebClient.get()
                        .uri(path)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .retrieve()
                        .onStatus(status -> status == HttpStatus.UNAUTHORIZED,
                                response -> Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized")))
                        .bodyToMono(String.class)
                );
    }

    /**
     * Ensures the access token is valid; if expired, refreshes it.
     * If refresh also fails, throws 401.
     */
    private Mono<String> ensureValidToken(WebSession session) {
        if (!tokenSessionService.hasTokens(session)) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No active session"));
        }

        if (tokenSessionService.isAccessTokenExpired(session)) {
            log.debug("Access token expired, attempting refresh...");
            return authService.refreshAccessToken(session)
                    .flatMap(success -> {
                        if (success) {
                            log.debug("Token refreshed successfully");
                            return Mono.just(tokenSessionService.getAccessToken(session));
                        }
                        return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                                "Session expired. Please login again."));
                    });
        }

        return Mono.just(tokenSessionService.getAccessToken(session));
    }
}
