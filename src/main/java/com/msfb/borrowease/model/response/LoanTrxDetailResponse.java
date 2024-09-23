package com.msfb.borrowease.model.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanTrxDetailResponse {
    private String id;
    private String loanId;
    private String dueDate;
    private int paymentAmount;
    private String status;
}
