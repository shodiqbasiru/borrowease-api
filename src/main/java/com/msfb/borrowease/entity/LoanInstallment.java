package com.msfb.borrowease.entity;

import com.msfb.borrowease.constant.ELoanInstallment;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "m_loan_installment")
public class LoanInstallment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "installment", nullable = false)
    @Enumerated(EnumType.STRING)
    private ELoanInstallment loanInstallment;
}
