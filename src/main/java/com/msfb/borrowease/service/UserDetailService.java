package com.msfb.borrowease.service;

import com.msfb.borrowease.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserDetailService extends UserDetailsService {
    User getUserById(String id);
    User getByContext();
}
