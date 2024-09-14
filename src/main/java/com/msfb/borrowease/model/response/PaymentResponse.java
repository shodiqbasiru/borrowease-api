package com.msfb.borrowease.model.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {
    private String loanTrxDetailId;
    private Double amount;
    private String paymentDate;
    private String status;
}
