package com.msfb.borrowease.model.response;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanResponse {
    private String id;
    private String customerId;
    private String loanType;
    private Double amount;
    private Integer termMonth;
    private String installment;
    private String interestRate;
    private Double installmentAmount;
    private List<LoanTrxDetailResponse> loanTrxDetails;
}
