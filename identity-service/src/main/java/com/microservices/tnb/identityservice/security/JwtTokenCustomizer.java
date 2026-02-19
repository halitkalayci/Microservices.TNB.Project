package com.microservices.tnb.identityservice.security;

import com.microservices.tnb.identityservice.entity.User;
import com.microservices.tnb.identityservice.entity.UserOperationClaim;
import com.microservices.tnb.identityservice.repository.UserOperationClaimRepository;
import com.microservices.tnb.identityservice.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class JwtTokenCustomizer {

    private final UserRepository userRepository;
    private final UserOperationClaimRepository userOperationClaimRepository;

    public JwtTokenCustomizer(UserRepository userRepository,
                               UserOperationClaimRepository userOperationClaimRepository) {
        this.userRepository = userRepository;
        this.userOperationClaimRepository = userOperationClaimRepository;
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return context -> {
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                String username = context.getPrincipal().getName();

                userRepository.findByUsername(username).ifPresent(user -> {
                    context.getClaims().claim("userId", user.getId().toString());
                    context.getClaims().claim("email", user.getEmail());

                    List<UserOperationClaim> userClaims =
                            userOperationClaimRepository.findByUserId(user.getId());

                    List<String> operations = userClaims.stream()
                            .map(claim -> claim.getOperationClaim().getName())
                            .collect(Collectors.toList());

                    context.getClaims().claim("operations", operations);
                });
            }
        };
    }
}
