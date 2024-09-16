package com.msfb.borrowease.entity;

import com.msfb.borrowease.constant.ELoanInstallment;
import com.msfb.borrowease.constant.ELoanProcess;
import com.msfb.borrowease.constant.ELoanType;
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

    @Column(name = "loan_trx_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date loanTrxDate;

    @Column(name = "loan_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ELoanType loanType;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name="term_month", nullable = false)
    private Integer termMonth;

    @Column(name = "installment", nullable = false)
    @Enumerated(EnumType.STRING)
    private ELoanInstallment installment;

    @Column(name = "interest_rate", nullable = false)
    private Double interestRate;

    @Column(name = "installment_amount", nullable = false)
    private int installmentAmount;

    @Column(name="loan_process", nullable = false)
    @Enumerated(EnumType.STRING)
    private ELoanProcess loanProcess;

//    @ManyToOne
//    @JoinColumn(name = "loan_type_id", nullable = false)
//    private LoanType loanType;

//    @ManyToOne
//    @JoinColumn(name = "loan_installment_id", nullable = false)
//    private LoanInstallment loanInstallment;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "loanTrx")
    private List<LoanTrxDetail> loanTrxDetails;

}
