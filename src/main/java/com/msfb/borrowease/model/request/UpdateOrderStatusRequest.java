package com.msfb.borrowease.model.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateOrderStatusRequest {
    private String paymentId;
    private String transactionStatus;
}
