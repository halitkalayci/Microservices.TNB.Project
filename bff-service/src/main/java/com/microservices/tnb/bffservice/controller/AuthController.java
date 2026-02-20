package com.microservices.tnb.bffservice.controller;

import com.microservices.tnb.bffservice.dto.LoginRequest;
import com.microservices.tnb.bffservice.dto.LoginResponse;
import com.microservices.tnb.bffservice.dto.SessionInfoResponse;
import com.microservices.tnb.bffservice.service.AuthService;
import com.microservices.tnb.bffservice.service.TokenSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Session-based authentication endpoints")
public class AuthController {

    private final AuthService authService;
    private final TokenSessionService tokenSessionService;

    public AuthController(AuthService authService, TokenSessionService tokenSessionService) {
        this.authService = authService;
        this.tokenSessionService = tokenSessionService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login with username/password, returns session cookie")
    public Mono<ResponseEntity<LoginResponse>> login(@RequestBody LoginRequest request, WebSession session) {
        return authService.login(request, session)
                .map(result -> {
                    boolean success = Boolean.TRUE.equals(result.get("success"));
                    if (success) {
                        return ResponseEntity.ok(LoginResponse.builder()
                                .success(true)
                                .username(request.getUsername())
                                .message("Login successful")
                                .build());
                    }
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(LoginResponse.builder()
                                    .success(false)
                                    .message((String) result.get("message"))
                                    .build());
                });
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout and invalidate session")
    public Mono<ResponseEntity<Void>> logout(WebSession session) {
        return tokenSessionService.clearTokens(session)
                .then(Mono.just(ResponseEntity.ok().<Void>build()));
    }

    @GetMapping("/session")
    @Operation(summary = "Check if session is valid and get user info")
    public Mono<ResponseEntity<SessionInfoResponse>> checkSession(WebSession session) {
        if (tokenSessionService.hasTokens(session)) {
            return Mono.just(ResponseEntity.ok(SessionInfoResponse.builder()
                    .authenticated(true)
                    .username(tokenSessionService.getUsername(session))
                    .build()));
        }
        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(SessionInfoResponse.builder()
                        .authenticated(false)
                        .build()));
    }
}
