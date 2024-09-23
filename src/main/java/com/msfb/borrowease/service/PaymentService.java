package com.msfb.borrowease.service;

import com.msfb.borrowease.entity.LoanTrx;
import com.msfb.borrowease.entity.Payment;
import com.msfb.borrowease.model.request.PaymentLoanRequest;

import java.util.List;

public interface PaymentService {
    Payment createNewPayment(LoanTrx trx, List<PaymentLoanRequest> loanRequests);
    void checkFailedAndUpdateStatus();
    Payment getById(String paymentId);
}
