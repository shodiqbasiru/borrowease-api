package com.msfb.borrowease.service.impl;

import com.msfb.borrowease.entity.Customer;
import com.msfb.borrowease.entity.User;
import com.msfb.borrowease.mapping.CustomerMapping;
import com.msfb.borrowease.model.request.CustomerRequest;
import com.msfb.borrowease.model.response.CustomerResponse;
import com.msfb.borrowease.repository.CustomerRepository;
import com.msfb.borrowease.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

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

    @Transactional(readOnly = true)
    @Override
    public Customer getById(String id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public CustomerResponse getCustomerById(String id) {
        Customer customer = getById(id);
        return CustomerMapping.toCustomerResponse(customer);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CustomerResponse> getAllCustomers() {
        List<Customer> customers = repository.findAll();
        return CustomerMapping.toCustomerResponses(customers);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CustomerResponse updateCustomer(CustomerRequest request) {
        Customer currentCustomer = getById(request.getId());
        currentCustomer.setFirstName(request.getFirstName());
        currentCustomer.setLastName(request.getLastName());
        currentCustomer.setEmail(request.getEmail());
        currentCustomer.setPhoneNumber(request.getPhoneNumber());
        currentCustomer.setAddress(request.getAddress());
        currentCustomer.setUpdatedAt(new Date());

        Customer updatedCustomer = repository.saveAndFlush(currentCustomer);

        return CustomerMapping.toCustomerResponse(updatedCustomer);
    }
}
