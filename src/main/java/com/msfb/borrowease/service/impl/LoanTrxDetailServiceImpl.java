package com.msfb.borrowease.service.impl;

import com.msfb.borrowease.entity.LoanTrxDetail;
import com.msfb.borrowease.repository.LoanTrxDetailRepository;
import com.msfb.borrowease.service.LoanTrxDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoanTrxDetailServiceImpl implements LoanTrxDetailService {

    private final LoanTrxDetailRepository repository;

    @Autowired
    public LoanTrxDetailServiceImpl(LoanTrxDetailRepository repository) {
        this.repository = repository;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LoanTrxDetail createLoanTrxDetail(LoanTrxDetail loanTrxDetail) {
        return repository.saveAndFlush(loanTrxDetail);
    }
}
