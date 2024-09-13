package com.msfb.borrowease.service;

import com.msfb.borrowease.model.request.LoanRequest;
import com.msfb.borrowease.model.response.LoanResponse;

public interface LoanTrxService {
    LoanResponse createLoanTrx(LoanRequest request);
}
