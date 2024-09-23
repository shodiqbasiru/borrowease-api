package com.msfb.borrowease.service.impl;

import com.msfb.borrowease.entity.Customer;
import com.msfb.borrowease.entity.LoanLimit;
import com.msfb.borrowease.repository.LoanLimitRepository;
import com.msfb.borrowease.service.CustomerService;
import com.msfb.borrowease.service.LoanLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LoanLimitServiceImpl implements LoanLimitService {

    private final LoanLimitRepository repository;
    private final CustomerService customerService;

    @Autowired
    public LoanLimitServiceImpl(LoanLimitRepository repository, CustomerService customerService) {
        this.repository = repository;
        this.customerService = customerService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveLoanLimit(LoanLimit loanLimit) {
        repository.saveAndFlush(loanLimit);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LoanLimit getById(String id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan Limit not found"));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void increaseLoanLimit(String customerId, int amount) {
        Customer customer = customerService.getById(customerId);
        LoanLimit loanLimit = getById(customer.getLoanLimit().getId());
        loanLimit.setCurrentLimit(loanLimit.getCurrentLimit() + amount);
        saveLoanLimit(loanLimit);
    }
}
