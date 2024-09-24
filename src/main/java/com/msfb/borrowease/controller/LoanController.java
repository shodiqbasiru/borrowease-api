package com.msfb.borrowease.controller;

import com.msfb.borrowease.constant.ApiRoute;
import com.msfb.borrowease.model.request.LoanApprovalRequest;
import com.msfb.borrowease.model.request.LoanRequest;
import com.msfb.borrowease.model.request.PaymentLoanRequest;
import com.msfb.borrowease.model.request.UpdateOrderStatusRequest;
import com.msfb.borrowease.model.response.ApplicationResponse;
import com.msfb.borrowease.model.response.CommonResponse;
import com.msfb.borrowease.model.response.LoanResponse;
import com.msfb.borrowease.model.response.PaymentResponse;
import com.msfb.borrowease.service.LoanTrxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            path = "create-application-loan",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<ApplicationResponse>> createLoanTrx(@RequestBody LoanRequest request) {
        ApplicationResponse applicationResponse = loanTrxService.createLoanApplication(request);
        CommonResponse<ApplicationResponse> response = CommonResponse.<ApplicationResponse>builder()
                .message("Loan transaction created successfully")
                .statusCode(HttpStatus.CREATED.value())
                .data(applicationResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(
            path = "approval-loan",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<LoanResponse>> approvalLoan(@RequestBody LoanApprovalRequest request) {
        LoanResponse loanResponse = loanTrxService.createLoanApproval(request);
        CommonResponse<LoanResponse> response = CommonResponse.<LoanResponse>builder()
                .message("Loan transaction created successfully")
                .statusCode(HttpStatus.OK.value())
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
        loanTrxService.updateStatusAndLimit(updateOrderStatusRequest);
        return ResponseEntity.ok(CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Loan transaction status updated successfully")
                .build());
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<List<LoanResponse>>> getLoanTrx() {
        List<LoanResponse> loanResponses = loanTrxService.getAllLoanTrx();
        CommonResponse<List<LoanResponse>> response = CommonResponse.<List<LoanResponse>>builder()
                .message("Loan transaction created successfully")
                .statusCode(HttpStatus.OK.value())
                .data(loanResponses)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<LoanResponse>> getLoanTrxById(@PathVariable String id) {
        LoanResponse loanResponse = loanTrxService.getLoanTrxById(id);
        CommonResponse<LoanResponse> response = CommonResponse.<LoanResponse>builder()
                .message("Loan transaction created successfully")
                .statusCode(HttpStatus.OK.value())
                .data(loanResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
