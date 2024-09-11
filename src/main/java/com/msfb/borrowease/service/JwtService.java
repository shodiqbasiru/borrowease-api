package com.msfb.borrowease.service;

import com.msfb.borrowease.entity.JwtClaims;
import com.msfb.borrowease.entity.User;

public interface JwtService {
    String generateJwtToken(User user);
    JwtClaims getClaimsByToken(String token);
}
