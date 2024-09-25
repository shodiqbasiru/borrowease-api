package com.msfb.borrowease.service;

import com.msfb.borrowease.model.request.LoginRequest;
import com.msfb.borrowease.model.request.RegisterRequest;
import com.msfb.borrowease.model.request.RegisterStaffRequest;
import com.msfb.borrowease.model.response.LoginResponse;
import com.msfb.borrowease.model.response.RegisterResponse;
import com.msfb.borrowease.model.response.RegisterStaffResponse;

public interface AuthService {
    RegisterResponse registerCustomer(RegisterRequest request);
    RegisterStaffResponse registerStaff(RegisterStaffRequest request);
    LoginResponse loginCustomer(LoginRequest request);
}
