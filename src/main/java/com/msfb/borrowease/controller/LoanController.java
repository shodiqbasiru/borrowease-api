package com.msfb.borrowease.controller;

import com.msfb.borrowease.constant.ApiRoute;
import com.msfb.borrowease.model.request.LoanRequest;
import com.msfb.borrowease.model.request.PaymentRequest;
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
    public ResponseEntity<CommonResponse<List<PaymentResponse>>> paymentLoan(@RequestBody List<PaymentRequest> requests) {
        List<PaymentResponse> paymentLoanResponse = loanTrxService.createPaymentLoan(requests);
        CommonResponse<List<PaymentResponse>> response = CommonResponse.<List<PaymentResponse>>builder()
                .message("Loan transaction created successfully")
                .statusCode(HttpStatus.CREATED.value())
                .data(paymentLoanResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
