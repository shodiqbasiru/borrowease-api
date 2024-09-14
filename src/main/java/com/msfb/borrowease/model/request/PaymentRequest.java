package com.msfb.borrowease.model.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {
    private String loanTrxDetailId;
    private Double amount;
}
