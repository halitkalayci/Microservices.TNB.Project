package com.microservices.tnb.identityservice.service;

import com.microservices.tnb.identityservice.entity.User;
import com.microservices.tnb.identityservice.entity.UserOperationClaim;
import com.microservices.tnb.identityservice.repository.UserOperationClaimRepository;
import com.microservices.tnb.identityservice.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserOperationClaimRepository userOperationClaimRepository;

    public CustomUserDetailsService(UserRepository userRepository,
                                     UserOperationClaimRepository userOperationClaimRepository) {
        this.userRepository = userRepository;
        this.userOperationClaimRepository = userOperationClaimRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Kullanıcı bulunamadı: " + username));

        List<UserOperationClaim> userClaims = userOperationClaimRepository.findByUserId(user.getId());

        List<GrantedAuthority> authorities = userClaims.stream()
                .map(claim -> new SimpleGrantedAuthority(claim.getOperationClaim().getName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
