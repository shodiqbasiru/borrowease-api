package com.msfb.borrowease.service;

import com.msfb.borrowease.entity.LoanTrx;
import com.msfb.borrowease.model.request.LoanApprovalRequest;
import com.msfb.borrowease.model.request.LoanRequest;
import com.msfb.borrowease.model.request.PaymentLoanRequest;
import com.msfb.borrowease.model.request.UpdateOrderStatusRequest;
import com.msfb.borrowease.model.response.ApplicationResponse;
import com.msfb.borrowease.model.response.LoanResponse;
import com.msfb.borrowease.model.response.PaymentResponse;

import java.util.List;

public interface LoanTrxService {
    ApplicationResponse createLoanApplication(LoanRequest request);
    LoanResponse createLoanApproval(LoanApprovalRequest request);
    PaymentResponse createPaymentLoan(List<PaymentLoanRequest> requests);
    LoanTrx getById(String id);
    List<LoanResponse> getAllLoanTrx();
    void updateStatusAndLimit(UpdateOrderStatusRequest request);
}
