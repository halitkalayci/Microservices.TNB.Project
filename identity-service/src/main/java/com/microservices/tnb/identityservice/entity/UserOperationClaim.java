package com.microservices.tnb.identityservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.UUID;

@Entity
@Table(name = "user_operation_claims",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "operation_claim_id"}))
public class UserOperationClaim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "operation_claim_id", nullable = false)
    private OperationClaim operationClaim;

    public UserOperationClaim() {
    }

    public UserOperationClaim(User user, OperationClaim operationClaim) {
        this.user = user;
        this.operationClaim = operationClaim;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OperationClaim getOperationClaim() {
        return operationClaim;
    }

    public void setOperationClaim(OperationClaim operationClaim) {
        this.operationClaim = operationClaim;
    }
}
