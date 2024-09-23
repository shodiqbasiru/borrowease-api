package com.msfb.borrowease.model.response;

import lombok.*;

import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {
    private String id;
    private String token;
    private String redirectUrl;
    private String loanTrxStatus;
    private List<PaymentDetailResponse> detailPayments;
}
