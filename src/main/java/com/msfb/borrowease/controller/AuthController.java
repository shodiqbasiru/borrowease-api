package com.msfb.borrowease.controller;

import com.msfb.borrowease.constant.ApiRoute;
import com.msfb.borrowease.model.request.LoginRequest;
import com.msfb.borrowease.model.request.RegisterRequest;
import com.msfb.borrowease.model.request.RegisterStaffRequest;
import com.msfb.borrowease.model.response.CommonResponse;
import com.msfb.borrowease.model.response.LoginResponse;
import com.msfb.borrowease.model.response.RegisterResponse;
import com.msfb.borrowease.model.response.RegisterStaffResponse;
import com.msfb.borrowease.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiRoute.AUTH_API)
@Tag(name = "Auth", description = "Auth API")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Register new user",
            description = "API to register new user"
    )
    @SecurityRequirement(name = "Authorization")
    @PostMapping(
            path = "/register",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<RegisterResponse>> register(@RequestBody RegisterRequest request) {
        RegisterResponse registerResponse = authService.registerCustomer(request);
        CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                .message("Success create account")
                .statusCode(HttpStatus.CREATED.value())
                .data(registerResponse)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Register new staff",
            description = "API to register new staff"
    )
    @SecurityRequirement(name = "Authorization")
    @PostMapping(
            path = "/register-staff",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<RegisterStaffResponse>> registerStaff(@RequestBody RegisterStaffRequest request) {
        RegisterStaffResponse registerResponse = authService.registerStaff(request);
        CommonResponse<RegisterStaffResponse> response = CommonResponse.<RegisterStaffResponse>builder()
                .message("Success create staff account")
                .statusCode(HttpStatus.CREATED.value())
                .data(registerResponse)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Login",
            description = "API to login"
    )
    @SecurityRequirement(name = "Authorization")
    @PostMapping(
            path = "/login",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.loginCustomer(request);
        CommonResponse<LoginResponse> response = CommonResponse.<LoginResponse>builder()
                .message("Success login")
                .statusCode(HttpStatus.OK.value())
                .data(loginResponse)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
