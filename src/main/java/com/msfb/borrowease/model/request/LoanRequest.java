package com.msfb.borrowease.model.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanRequest {
    private String customerId;
    private String loanType;
    private Integer amount;
    private Integer termMonth;
}
