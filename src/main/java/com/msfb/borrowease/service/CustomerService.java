package com.msfb.borrowease.service;

import com.msfb.borrowease.entity.Customer;
import com.msfb.borrowease.model.request.CustomerRequest;
import com.msfb.borrowease.model.response.CustomerResponse;

import java.util.List;

public interface CustomerService {
    Customer createCustomer(Customer customer);
    Customer getById(String id);
    CustomerResponse getCustomerById(String id);
    List<CustomerResponse> getAllCustomers();
    CustomerResponse updateCustomer(CustomerRequest request);
}
