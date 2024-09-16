package com.msfb.borrowease.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {
    @JsonProperty("transaction_details")
    private PaymentDetailRequest paymentDetail;

    @JsonProperty("item_details")
    private List<PaymentItemDetailRequest> paymentItemDetails;

    @JsonProperty("enabled_payments")
    List<String> paymentMethods;

}
