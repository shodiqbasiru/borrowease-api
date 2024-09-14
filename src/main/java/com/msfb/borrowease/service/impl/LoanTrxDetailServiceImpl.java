package com.msfb.borrowease.service.impl;

import com.msfb.borrowease.entity.LoanTrxDetail;
import com.msfb.borrowease.repository.LoanTrxDetailRepository;
import com.msfb.borrowease.service.LoanTrxDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

    @Transactional(readOnly = true)
    @Override
    public LoanTrxDetail getById(String id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan trx detail not found"));
    }
}
