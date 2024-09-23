package com.msfb.borrowease.model.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentLoanRequest {
    private String loanTrxDetailId;
    private Integer amount;
}
