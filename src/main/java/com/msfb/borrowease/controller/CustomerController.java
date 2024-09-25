package com.msfb.borrowease.controller;

import com.msfb.borrowease.constant.ApiRoute;
import com.msfb.borrowease.model.request.CustomerRequest;
import com.msfb.borrowease.model.response.CommonResponse;
import com.msfb.borrowease.model.response.CustomerResponse;
import com.msfb.borrowease.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiRoute.CUSTOMER_API)
@Tag(name = "Customer", description = "Customer API")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(
            summary = "Get customer by id",
            description = "API to get customer by id"
    )
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<CustomerResponse>> getCustomerById(@PathVariable String id) {
        CustomerResponse customerResponse = customerService.getCustomerById(id);
        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Customer retrieved successfully")
                .data(customerResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get all customers",
            description = "API to get all customers"
    )
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<List<CustomerResponse>>> getAllCustomers() {
        List<CustomerResponse> customerResponses = customerService.getAllCustomers();
        CommonResponse<List<CustomerResponse>> response = CommonResponse.<List<CustomerResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Customers retrieved successfully")
                .data(customerResponses)
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update customer",
            description = "API to update customer"
    )
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<CustomerResponse>> updateCustomer(@RequestBody CustomerRequest request) {
        CustomerResponse customerResponse = customerService.updateCustomer(request);
        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Customer updated successfully")
                .data(customerResponse)
                .build();
        return ResponseEntity.ok(response);
    }
}
