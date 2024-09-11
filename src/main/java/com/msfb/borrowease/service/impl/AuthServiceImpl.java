package com.msfb.borrowease.service.impl;

import com.msfb.borrowease.constant.ERole;
import com.msfb.borrowease.entity.Customer;
import com.msfb.borrowease.entity.LoanLimit;
import com.msfb.borrowease.entity.User;
import com.msfb.borrowease.model.request.RegisterRequest;
import com.msfb.borrowease.model.response.RegisterResponse;
import com.msfb.borrowease.repository.UserRepository;
import com.msfb.borrowease.service.AuthService;
import com.msfb.borrowease.service.CustomerService;
import com.msfb.borrowease.service.LoanLimitService;
import com.msfb.borrowease.util.DateUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository repository;
    private final LoanLimitService loanLimitService;
    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;

    @Value("${borrow_ease.username.admin}")
    private String adminUsername;

    @Value("${borrow_ease.password.admin}")
    private String adminPassword;

    private static final Double INITIAL_LIMIT = 10000000.0;

    @Autowired
    public AuthServiceImpl(UserRepository repository, LoanLimitService loanLimitService, CustomerService customerService, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.loanLimitService = loanLimitService;
        this.customerService = customerService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(rollbackFor = Exception.class)
    @PostConstruct
    public void initAdmin() {
        Optional<User> user = repository.findByUsername(adminUsername);
        if (user.isPresent()) return;

        String hashPassword = passwordEncoder.encode(adminPassword);
        ERole roleAdmin = ERole.ROLE_ADMIN;

        User admin = User.builder()
                .username(adminUsername)
                .password(hashPassword)
                .role(roleAdmin)
                .isEnable(true)
                .build();
        repository.saveAndFlush(admin);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RegisterResponse registerCustomer(RegisterRequest request) {
        String hashPassword = passwordEncoder.encode(request.getPassword());
        ERole roleCustomer = ERole.ROLE_CUSTOMER;

        User newUser = User.builder()
                .username(request.getUsername())
                .password(hashPassword)
                .role(roleCustomer)
                .isEnable(true)
                .build();
        User user = repository.saveAndFlush(newUser);

        LoanLimit loanLimit = LoanLimit.builder()
                .initialLimit(INITIAL_LIMIT)
                .currentLimit(INITIAL_LIMIT)
                .build();
        loanLimitService.createLoanLimit(loanLimit);


        Date birthDate = DateUtil.parseDate(request.getBirthDate(), "yyyy-MM-dd");

        Customer newCustomer = Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .user(newUser)
                .loanLimit(loanLimit)
                .birthDate(birthDate)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        Customer customer = customerService.createCustomer(newCustomer);

        String birthDateResponse = DateUtil.formatDate(customer.getBirthDate(), "yyyy-MM-dd");
        String cratedAtResponse = DateUtil.formatDate(customer.getCreatedAt(), "yyyy-MM-dd HH:mm:ss");
        String updatedAtResponse = DateUtil.formatDate(customer.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss");

        return RegisterResponse.builder()
                .id(user.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .username(user.getUsername())
                .role(user.getRole().name())
                .phoneNumber(customer.getPhoneNumber())
                .birthDate(birthDateResponse)
                .createdAt(cratedAtResponse)
                .updatedAt(updatedAtResponse)
                .build();
    }
}
