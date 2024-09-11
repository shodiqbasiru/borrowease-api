package com.msfb.borrowease.service;

import com.msfb.borrowease.model.request.RegisterRequest;
import com.msfb.borrowease.model.response.RegisterResponse;

public interface AuthService {
    RegisterResponse registerCustomer(RegisterRequest request);
}
