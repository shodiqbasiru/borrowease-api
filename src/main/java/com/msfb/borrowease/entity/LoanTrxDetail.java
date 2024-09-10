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
@Table(name = "t_loan_trx_detail")
public class LoanTrxDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "transaction_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;

    @Column(name = "payment_amount", nullable = false)
    private Double paymentAmount;

    @Column(name = "principal_amount", nullable = false)
    private Double principalAmount;

    @Column(name = "interest_amount", nullable = false)
    private Double interestAmount;

    @Column(name = "late_fee", nullable = false)
    private Double lateFee;

    @Column(name = "remaining_balance", nullable = false)
    private Double remainingBalance;

    @ManyToOne
    @JoinColumn(name = "loan_trx_id", nullable = false)
    private LoanTrx loanTrx;
}
