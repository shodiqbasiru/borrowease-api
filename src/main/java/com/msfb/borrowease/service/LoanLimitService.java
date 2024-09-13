package com.msfb.borrowease.service;

import com.msfb.borrowease.entity.LoanLimit;

public interface LoanLimitService {
    void createLoanLimit(LoanLimit loanLimit);
    LoanLimit getById(String id);
    void updateLoanLimit(LoanLimit loanLimit);
}
