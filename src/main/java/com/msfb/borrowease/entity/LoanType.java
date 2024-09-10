package com.msfb.borrowease.entity;

import com.msfb.borrowease.constant.ELoanStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "m_loan_type")
public class LoanType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "loan_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ELoanStatus loanType;
}
