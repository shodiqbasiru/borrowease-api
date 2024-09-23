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
    private int amount;
    private Integer termMonth;
    private String installment;
    private String interestRate;
    private int installmentAmount;
    private List<LoanTrxDetailResponse> loanTrxDetails;
}
