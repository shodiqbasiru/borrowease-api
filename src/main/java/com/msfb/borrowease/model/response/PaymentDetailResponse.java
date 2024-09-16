package com.msfb.borrowease.model.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDetailResponse {
    private String id;
    private Integer amount;
}
