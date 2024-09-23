package com.msfb.borrowease.mapping;

import com.msfb.borrowease.entity.Customer;
import com.msfb.borrowease.model.response.CustomerResponse;
import com.msfb.borrowease.util.DateUtil;

import java.util.List;

public class CustomerMapping {
    public static CustomerResponse toCustomerResponse(Customer customer) {
        String createdAt = DateUtil.formatDate(customer.getCreatedAt(), "yyyy-MM-dd HH:mm:ss");
        String updatedAt = DateUtil.formatDate(customer.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss");

        return CustomerResponse.builder()
                .Id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .address(customer.getAddress())
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public static List<CustomerResponse> toCustomerResponses(List<Customer> customers){
        return customers.stream().map(CustomerMapping::toCustomerResponse).toList();
    }
}
