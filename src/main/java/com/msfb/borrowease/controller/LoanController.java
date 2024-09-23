package com.msfb.borrowease.controller;

import com.msfb.borrowease.constant.ApiRoute;
import com.msfb.borrowease.model.request.LoanRequest;
import com.msfb.borrowease.model.request.PaymentLoanRequest;
import com.msfb.borrowease.model.request.UpdateOrderStatusRequest;
import com.msfb.borrowease.model.response.CommonResponse;
import com.msfb.borrowease.model.response.LoanResponse;
import com.msfb.borrowease.model.response.PaymentResponse;
import com.msfb.borrowease.service.LoanTrxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(ApiRoute.LOAN_API)
public class LoanController {
    private final LoanTrxService loanTrxService;

    @Autowired
    public LoanController(LoanTrxService loanTrxService) {
        this.loanTrxService = loanTrxService;
    }

    @PostMapping(
            path = "create-loan-trx",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<LoanResponse>> createLoanTrx(@RequestBody LoanRequest request) {
        LoanResponse loanResponse = loanTrxService.createLoanTrx(request);
        CommonResponse<LoanResponse> response = CommonResponse.<LoanResponse>builder()
                .message("Loan transaction created successfully")
                .statusCode(HttpStatus.CREATED.value())
                .data(loanResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(
            path = "payment-loan",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<PaymentResponse>> paymentLoan(@RequestBody List<PaymentLoanRequest> requests) {
        PaymentResponse paymentLoanResponse = loanTrxService.createPaymentLoan(requests);
        CommonResponse<PaymentResponse> response = CommonResponse.<PaymentResponse>builder()
                .message("Loan transaction created successfully")
                .statusCode(HttpStatus.CREATED.value())
                .data(paymentLoanResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/status")
    public ResponseEntity<CommonResponse<?>> updateStatus(@RequestBody Map<String, String> request) {
        UpdateOrderStatusRequest updateOrderStatusRequest = UpdateOrderStatusRequest.builder()
                .paymentId(request.get("order_id"))
                .transactionStatus(request.get("transaction_status"))
                .build();
        loanTrxService.updateStatus(updateOrderStatusRequest);
        return ResponseEntity.ok(CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Loan transaction status updated successfully")
                .build());
    }
}
