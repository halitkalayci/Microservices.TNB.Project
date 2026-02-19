package com.microservices.tnb.identityservice.repository;

import com.microservices.tnb.identityservice.entity.UserOperationClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserOperationClaimRepository extends JpaRepository<UserOperationClaim, Long> {

    List<UserOperationClaim> findByUserId(UUID userId);
}
