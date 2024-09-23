package com.msfb.borrowease.service;

import com.msfb.borrowease.entity.LoanLimit;

public interface LoanLimitService {
    void saveLoanLimit(LoanLimit loanLimit);
    LoanLimit getById(String id);
    void increaseLoanLimit(String customerId, int amount);
}
