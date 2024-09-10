package com.msfb.borrowease.entity;

import com.msfb.borrowease.constant.ELoanProcess;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "t_loan_trx")
public class LoanTrx {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "interest_rate", nullable = false)
    private Double interestRate;

    @Column(name="term_month", nullable = false)
    private Integer termMonth;

    @Column(name = "installment_amount", nullable = false)
    private Double installmentAmount;

    @Column(name = "start_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Column(name="loan_process", nullable = false)
    @Enumerated(EnumType.STRING)
    private ELoanProcess loanProcess;

    @ManyToOne
    @JoinColumn(name = "loan_type_id", nullable = false)
    private LoanType loanType;

    @ManyToOne
    @JoinColumn(name = "loan_installment_id", nullable = false)
    private LoanInstallment loanInstallment;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "loanTrx")
    private List<LoanTrxDetail> loanTrxDetails;

}
