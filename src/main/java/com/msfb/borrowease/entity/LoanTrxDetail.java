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
@Table(name = "t_loan_trx_detail")
public class LoanTrxDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "start_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Column(name = "payment_amount", nullable = false)
    private Integer paymentAmount;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ELoanStatus status;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "loan_trx_id", nullable = false)
    private LoanTrx loanTrx;
}
