package com.msfb.borrowease.service;

import com.msfb.borrowease.entity.LoanTrx;
import com.msfb.borrowease.model.request.LoanRequest;
import com.msfb.borrowease.model.request.PaymentLoanRequest;
import com.msfb.borrowease.model.response.LoanResponse;
import com.msfb.borrowease.model.response.PaymentResponse;

import java.util.List;

public interface LoanTrxService {
    LoanResponse createLoanTrx(LoanRequest request);
    PaymentResponse createPaymentLoan(List<PaymentLoanRequest> requests);
    LoanTrx getById(String id);
}
