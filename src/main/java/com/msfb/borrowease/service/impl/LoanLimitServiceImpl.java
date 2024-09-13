package com.msfb.borrowease.service.impl;

import com.msfb.borrowease.entity.LoanLimit;
import com.msfb.borrowease.repository.LoanLimitRepository;
import com.msfb.borrowease.service.LoanLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoanLimitServiceImpl implements LoanLimitService {

    private final LoanLimitRepository repository;

    @Autowired
    public LoanLimitServiceImpl(LoanLimitRepository repository) {
        this.repository = repository;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createLoanLimit(LoanLimit loanLimit) {
        repository.saveAndFlush(loanLimit);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LoanLimit getById(String id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Loan Limit not found"));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateLoanLimit(LoanLimit loanLimit) {
        repository.saveAndFlush(loanLimit);
    }
}
