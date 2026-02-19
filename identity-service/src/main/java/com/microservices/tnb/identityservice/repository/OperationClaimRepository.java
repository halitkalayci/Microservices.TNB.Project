package com.microservices.tnb.identityservice.repository;

import com.microservices.tnb.identityservice.entity.OperationClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OperationClaimRepository extends JpaRepository<OperationClaim, Long> {

    Optional<OperationClaim> findByName(String name);
}
