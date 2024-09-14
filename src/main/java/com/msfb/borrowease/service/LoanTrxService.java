package com.msfb.borrowease.service;

import com.msfb.borrowease.entity.LoanTrx;
import com.msfb.borrowease.model.request.LoanRequest;
import com.msfb.borrowease.model.request.PaymentRequest;
import com.msfb.borrowease.model.response.LoanResponse;
import com.msfb.borrowease.model.response.PaymentResponse;

import java.util.List;

public interface LoanTrxService {
    LoanResponse createLoanTrx(LoanRequest request);
    List<PaymentResponse> createPaymentLoan(List<PaymentRequest> requests);
    LoanTrx getById(String id);
}
