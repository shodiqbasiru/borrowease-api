package com.msfb.borrowease.entity;

import com.msfb.borrowease.constant.ELoanTrxStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "t_payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "token")
    private String token;

    @Column(name = "redirect_url")
    private String redirectUrl;

    @Enumerated(EnumType.STRING)
    private ELoanTrxStatus transactionStatus;

    @OneToMany(mappedBy = "payment")
    private List<LoanTrxDetail> loanTrxDetails;
}
