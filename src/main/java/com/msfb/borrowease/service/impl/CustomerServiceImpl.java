package com.msfb.borrowease.service.impl;

import com.msfb.borrowease.entity.Customer;
import com.msfb.borrowease.repository.CustomerRepository;
import com.msfb.borrowease.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Customer createCustomer(Customer customer) {
        return repository.saveAndFlush(customer);
    }
}
