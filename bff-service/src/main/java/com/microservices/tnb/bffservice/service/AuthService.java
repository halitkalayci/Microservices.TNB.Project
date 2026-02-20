package com.microservices.tnb.bffservice.service;

import com.microservices.tnb.bffservice.dto.LoginRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles the full OAuth2 Authorization Code + PKCE flow programmatically.
 * 
 * Flow:
 * 1. Generate code_verifier & code_challenge
 * 2. Call /oauth2/authorize → get redirect to /login
 * 3. Parse CSRF token from login page
 * 4. POST credentials to /login (maintaining cookies)
 * 5. Follow redirects to get authorization code
 * 6. Exchange code + code_verifier for tokens at /oauth2/token
 */
@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final WebClient identityWebClient;
    private final TokenSessionService tokenSessionService;

    @Value("${bff.oauth2.client-id}")
    private String clientId;

    @Value("${bff.oauth2.client-secret}")
    private String clientSecret;

    @Value("${bff.oauth2.redirect-uri}")
    private String redirectUri;

    @Value("${bff.oauth2.scopes}")
    private String scopes;

    @Value("${bff.identity-url}")
    private String identityUrl;

    public AuthService(@Qualifier("identityWebClient") WebClient identityWebClient,
                       TokenSessionService tokenSessionService) {
        this.identityWebClient = identityWebClient;
        this.tokenSessionService = tokenSessionService;
    }

    /**
     * Performs the complete PKCE login flow server-side.
     */
    public Mono<Map<String, Object>> login(LoginRequest loginRequest, WebSession session) {
        String codeVerifier = generateCodeVerifier();
        String codeChallenge = generateCodeChallenge(codeVerifier);

        // Step 1: Call /oauth2/authorize to get the login page redirect
        String authorizeUrl = String.format(
                "/oauth2/authorize?response_type=code&client_id=%s&redirect_uri=%s&code_challenge=%s&code_challenge_method=S256&scope=%s",
                clientId, redirectUri, codeChallenge, scopes.replace(",", "+"));

        return identityWebClient.get()
                .uri(authorizeUrl)
                .exchangeToMono(authorizeResponse -> {
                    // Should get 302 redirect to /login
                    if (authorizeResponse.statusCode().is3xxRedirection()) {
                        String loginRedirectUrl = authorizeResponse.headers().header(HttpHeaders.LOCATION).stream()
                                .findFirst().orElse("/login");
                        List<String> cookies = authorizeResponse.headers().header(HttpHeaders.SET_COOKIE);
                        String sessionCookie = extractSessionCookie(cookies);

                        log.debug("Authorize redirected to: {}", loginRedirectUrl);
                        return fetchLoginPageAndSubmit(loginRedirectUrl, sessionCookie,
                                loginRequest, codeVerifier);
                    }
                    return Mono.just(errorResult("Authorization server did not redirect to login page"));
                })
                .onErrorResume(e -> {
                    log.error("Login failed", e);
                    return Mono.just(errorResult("Login failed: " + e.getMessage()));
                })
                .flatMap(result -> {
                    if (Boolean.TRUE.equals(result.get("success"))) {
                        return tokenSessionService.storeTokens(
                                session,
                                (String) result.get("access_token"),
                                (String) result.get("refresh_token"),
                                result.get("expires_in") != null ? ((Number) result.get("expires_in")).longValue() : 900,
                                loginRequest.getUsername()
                        ).thenReturn(result);
                    }
                    return Mono.just(result);
                });
    }

    /**
     * Refreshes the access token using the stored refresh token.
     */
    public Mono<Boolean> refreshAccessToken(WebSession session) {
        String refreshToken = tokenSessionService.getRefreshToken(session);
        if (refreshToken == null) {
            return Mono.just(false);
        }

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "refresh_token");
        formData.add("refresh_token", refreshToken);
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);

        return identityWebClient.post()
                .uri("/oauth2/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(Map.class);
                    }
                    return response.releaseBody().then(Mono.empty());
                })
                .flatMap(tokenResponse -> {
                    if (tokenResponse != null) {
                        String newAccessToken = (String) tokenResponse.get("access_token");
                        String newRefreshToken = (String) tokenResponse.get("refresh_token");
                        Number expiresIn = (Number) tokenResponse.get("expires_in");
                        String username = tokenSessionService.getUsername(session);

                        return tokenSessionService.storeTokens(session, newAccessToken, newRefreshToken,
                                        expiresIn != null ? expiresIn.longValue() : 900, username)
                                .thenReturn(true);
                    }
                    return Mono.just(false);
                })
                .onErrorResume(e -> {
                    log.error("Token refresh failed", e);
                    return Mono.just(false);
                });
    }

    // ============ Private helpers for the PKCE flow ============

    private Mono<Map<String, Object>> fetchLoginPageAndSubmit(
            String loginUrl, String sessionCookie,
            LoginRequest loginRequest, String codeVerifier) {

        // Normalize URL - if it's absolute, extract path
        String path = loginUrl.startsWith("http") ? loginUrl.replace(identityUrl, "") : loginUrl;

        // Step 2: GET the login page to extract CSRF token
        return identityWebClient.get()
                .uri(path)
                .header(HttpHeaders.COOKIE, sessionCookie)
                .exchangeToMono(loginPageResponse -> {
                    List<String> newCookies = loginPageResponse.headers().header(HttpHeaders.SET_COOKIE);
                    String updatedCookie = mergeCookies(sessionCookie, newCookies);

                    return loginPageResponse.bodyToMono(String.class)
                            .flatMap(html -> {
                                String csrfToken = extractCsrfToken(html);
                                log.debug("CSRF token extracted: {}", csrfToken != null ? "yes" : "no");
                                return submitLoginForm(updatedCookie, loginRequest, csrfToken, codeVerifier);
                            });
                });
    }

    private Mono<Map<String, Object>> submitLoginForm(
            String sessionCookie, LoginRequest loginRequest,
            String csrfToken, String codeVerifier) {

        // Step 3: POST credentials to /login
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("username", loginRequest.getUsername());
        formData.add("password", loginRequest.getPassword());
        if (csrfToken != null) {
            formData.add("_csrf", csrfToken);
        }

        return identityWebClient.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header(HttpHeaders.COOKIE, sessionCookie)
                .body(BodyInserters.fromFormData(formData))
                .exchangeToMono(loginResponse -> {
                    if (loginResponse.statusCode().is3xxRedirection()) {
                        String redirectUrl = loginResponse.headers().header(HttpHeaders.LOCATION).stream()
                                .findFirst().orElse("");
                        List<String> newCookies = loginResponse.headers().header(HttpHeaders.SET_COOKIE);
                        String updatedCookie = mergeCookies(sessionCookie, newCookies);

                        log.debug("Login redirected to: {}", redirectUrl);

                        // Check if redirect is to /login?error (bad credentials)
                        if (redirectUrl.contains("/login?error")) {
                            return loginResponse.releaseBody()
                                    .then(Mono.just(errorResult("Invalid username or password")));
                        }

                        // Follow redirect chain to get authorization code
                        return loginResponse.releaseBody()
                                .then(followRedirectsForCode(redirectUrl, updatedCookie, codeVerifier));
                    }
                    return loginResponse.releaseBody()
                            .then(Mono.just(errorResult("Login did not redirect as expected")));
                });
    }

    private Mono<Map<String, Object>> followRedirectsForCode(
            String url, String sessionCookie, String codeVerifier) {

        // The redirect after successful login goes back to /oauth2/authorize
        // which then redirects to our redirect_uri with ?code=...
        String path = url.startsWith("http") ? url.replace(identityUrl, "") : url;

        return identityWebClient.get()
                .uri(path)
                .header(HttpHeaders.COOKIE, sessionCookie)
                .exchangeToMono(response -> {
                    if (response.statusCode().is3xxRedirection()) {
                        String location = response.headers().header(HttpHeaders.LOCATION).stream()
                                .findFirst().orElse("");
                        List<String> newCookies = response.headers().header(HttpHeaders.SET_COOKIE);
                        String updatedCookie = mergeCookies(sessionCookie, newCookies);

                        log.debug("Following redirect to: {}", location);

                        // Check if we got the authorization code in the redirect URI
                        if (location.contains("code=")) {
                            String code = extractAuthorizationCode(location);
                            log.debug("Authorization code obtained");
                            return response.releaseBody()
                                    .then(exchangeCodeForTokens(code, codeVerifier));
                        }

                        // Need to follow more redirects
                        return response.releaseBody()
                                .then(followRedirectsForCode(location, updatedCookie, codeVerifier));
                    }

                    return response.releaseBody()
                            .then(Mono.just(errorResult("Did not receive authorization code")));
                });
    }

    private Mono<Map<String, Object>> exchangeCodeForTokens(String code, String codeVerifier) {
        // Step 4: Exchange authorization code for tokens
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("code", code);
        formData.add("redirect_uri", redirectUri);
        formData.add("code_verifier", codeVerifier);
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);

        return identityWebClient.post()
                .uri("/oauth2/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(Map.class)
                                .map(tokenResponse -> {
                                    Map<String, Object> result = new java.util.HashMap<>();
                                    result.put("success", true);
                                    result.put("access_token", tokenResponse.get("access_token"));
                                    result.put("refresh_token", tokenResponse.get("refresh_token"));
                                    result.put("expires_in", tokenResponse.get("expires_in"));
                                    return result;
                                });
                    }
                    return response.bodyToMono(String.class)
                            .flatMap(body -> {
                                log.error("Token exchange failed: {}", body);
                                return Mono.just(errorResult("Token exchange failed"));
                            });
                });
    }

    // ============ PKCE Utilities ============

    private String generateCodeVerifier() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[64];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String generateCodeChallenge(String codeVerifier) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate code challenge", e);
        }
    }

    // ============ Cookie & HTML Parsing Helpers ============

    private String extractSessionCookie(List<String> setCookieHeaders) {
        StringBuilder cookies = new StringBuilder();
        for (String header : setCookieHeaders) {
            String cookiePart = header.split(";")[0];
            if (!cookies.isEmpty()) {
                cookies.append("; ");
            }
            cookies.append(cookiePart);
        }
        return cookies.toString();
    }

    private String mergeCookies(String existingCookies, List<String> newSetCookieHeaders) {
        // Parse existing cookies into a map
        Map<String, String> cookieMap = new java.util.LinkedHashMap<>();
        if (existingCookies != null && !existingCookies.isEmpty()) {
            for (String part : existingCookies.split(";\\s*")) {
                String[] kv = part.split("=", 2);
                if (kv.length == 2) {
                    cookieMap.put(kv[0].trim(), kv[1].trim());
                }
            }
        }

        // Override with new cookies
        for (String header : newSetCookieHeaders) {
            String cookiePart = header.split(";")[0];
            String[] kv = cookiePart.split("=", 2);
            if (kv.length == 2) {
                cookieMap.put(kv[0].trim(), kv[1].trim());
            }
        }

        StringBuilder result = new StringBuilder();
        cookieMap.forEach((key, value) -> {
            if (!result.isEmpty()) {
                result.append("; ");
            }
            result.append(key).append("=").append(value);
        });
        return result.toString();
    }

    private String extractCsrfToken(String html) {
        // Search for <input name="_csrf" type="hidden" value="TOKEN"/>
        // Use [^>]* to skip over any intermediate attributes (e.g. type="hidden")
        Pattern pattern = Pattern.compile("name=\"_csrf\"[^>]*value=\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            return matcher.group(1);
        }
        // Try alternate pattern (value before name)
        Pattern pattern2 = Pattern.compile("value=\"([^\"]+)\"[^>]*name=\"_csrf\"");
        Matcher matcher2 = pattern2.matcher(html);
        if (matcher2.find()) {
            return matcher2.group(1);
        }
        return null;
    }

    private String extractAuthorizationCode(String url) {
        Pattern pattern = Pattern.compile("[?&]code=([^&]+)");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new IllegalStateException("Could not extract authorization code from URL: " + url);
    }

    private Map<String, Object> errorResult(String message) {
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("success", false);
        result.put("message", message);
        return result;
    }
}
