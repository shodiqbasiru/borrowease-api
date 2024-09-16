package com.msfb.borrowease.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "m_loan_limit")
public class LoanLimit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "initial_limit", nullable = false)
    private Integer initialLimit;

    @Column(name = "current_limit", nullable = false)
    private Integer currentLimit;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
}