package com.msfb.borrowease.service.impl;

import com.msfb.borrowease.constant.EEducation;
import com.msfb.borrowease.constant.EIdentityCard;
import com.msfb.borrowease.constant.ERole;
import com.msfb.borrowease.constant.EStatus;
import com.msfb.borrowease.entity.*;
import com.msfb.borrowease.model.request.LoginRequest;
import com.msfb.borrowease.model.request.RegisterRequest;
import com.msfb.borrowease.model.response.LoginResponse;
import com.msfb.borrowease.model.response.RegisterResponse;
import com.msfb.borrowease.repository.UserRepository;
import com.msfb.borrowease.service.*;
import com.msfb.borrowease.util.DateUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private final JwtService jwtService;
    private final IdentityCardService identityCardService;
    private final JobService jobService;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Value("${borrow_ease.username.admin}")
    private String adminUsername;

    @Value("${borrow_ease.password.admin}")
    private String adminPassword;

    private static final int INITIAL_LIMIT = 10000000;

    @Autowired
    public AuthServiceImpl(UserRepository repository, LoanLimitService loanLimitService, CustomerService customerService, JwtService jwtService, IdentityCardService identityCardService, JobService jobService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.loanLimitService = loanLimitService;
        this.customerService = customerService;
        this.jwtService = jwtService;
        this.identityCardService = identityCardService;
        this.jobService = jobService;
        this.authenticationManager = authenticationManager;
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
        loanLimitService.saveLoanLimit(loanLimit);

        Job job = Job.builder()
                .jobName(request.getJobName())
                .companyName(request.getCompanyName())
                .salary(request.getSalary())
                .build();
        jobService.createJob(job);

        Date birthDate = DateUtil.parseDate(request.getBirthDate(), "yyyy-MM-dd");
        EStatus status = getStatus(request);
        EEducation education = getEducation(request);

        Customer newCustomer = Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .motherName(request.getMotherName())
                .status(status)
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .user(newUser)
                .loanLimit(loanLimit)
                .birthDate(birthDate)
                .lastEducation(education)
                .job(job)
                .npwp(request.getNpwp())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        Customer customer = customerService.createCustomer(newCustomer);

        EIdentityCard identity = getIdentityCard(request);

        IdentityCard newIdentityCard = IdentityCard.builder()
                .identityNumber(request.getIdentityNumber())
                .identityCard(identity)
                .customer(customer)
                .build();
        identityCardService.createIdentityCard(newIdentityCard);

        String birthDateResponse = DateUtil.formatDate(customer.getBirthDate(), "yyyy-MM-dd");
        String cratedAtResponse = DateUtil.formatDate(customer.getCreatedAt(), "yyyy-MM-dd HH:mm:ss");
        String updatedAtResponse = DateUtil.formatDate(customer.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss");

        return RegisterResponse.builder()
                .id(user.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .status(customer.getStatus().name())
                .username(user.getUsername())
                .role(user.getRole().name())
                .phoneNumber(customer.getPhoneNumber())
                .email(customer.getEmail())
                .identityCard(newIdentityCard.getIdentityCard().name())
                .jobName(customer.getJob().getJobName())
                .npwp(customer.getNpwp())
                .birthDate(birthDateResponse)
                .createdAt(cratedAtResponse)
                .updatedAt(updatedAtResponse)
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LoginResponse loginCustomer(LoginRequest request) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );

        Authentication authenticate = authenticationManager.authenticate(authentication);
        User user = (User) authenticate.getPrincipal();

        String jwtToken = jwtService.generateJwtToken(user);

        return LoginResponse.builder()
                .userId(user.getId())
                .customerId(user.getCustomer().getId())
                .username(user.getUsername())
                .role(user.getRole().name())
                .token(jwtToken)
                .build();
    }

    private static EStatus getStatus(RegisterRequest request) {
        EStatus status;
        if (request.getStatus().equalsIgnoreCase(EStatus.MARRIED.name())) {
            status = EStatus.MARRIED;
        } else if (request.getStatus().equalsIgnoreCase(EStatus.SINGLE.name())) {
            status = EStatus.SINGLE;
        } else if (request.getStatus().equalsIgnoreCase(EStatus.DIVORCED.name())) {
            status = EStatus.DIVORCED;
        } else if (request.getStatus().equalsIgnoreCase(EStatus.WIDOWED.name())) {
            status = EStatus.WIDOWED;
        } else {
            throw new IllegalStateException("Unexpected value: " + request.getStatus());
        }
        return status;
    }

    private static EEducation getEducation(RegisterRequest request) {
        EEducation education;
        if (request.getLastEducation().equalsIgnoreCase(EEducation.SD.name())) {
            education = EEducation.SD;
        } else if (request.getLastEducation().equalsIgnoreCase(EEducation.SMP.name())) {
            education = EEducation.SMP;
        } else if (request.getLastEducation().equalsIgnoreCase(EEducation.SMA.name())) {
            education = EEducation.SMA;
        } else if (request.getLastEducation().equalsIgnoreCase(EEducation.D3.name())) {
            education = EEducation.D3;
        } else if (request.getLastEducation().equalsIgnoreCase(EEducation.S1.name())) {
            education = EEducation.S1;
        } else if (request.getLastEducation().equalsIgnoreCase(EEducation.S2.name())) {
            education = EEducation.S2;
        } else if (request.getLastEducation().equalsIgnoreCase(EEducation.S3.name())) {
            education = EEducation.S3;
        } else {
            throw new IllegalStateException("Unexpected value: " + request.getLastEducation());
        }
        return education;
    }

    private static EIdentityCard getIdentityCard(RegisterRequest request) {
        EIdentityCard identityCard;
        if (request.getIdentityCard().equalsIgnoreCase(EIdentityCard.KTP.name())) {
            identityCard = EIdentityCard.KTP;
        } else if (request.getIdentityCard().equalsIgnoreCase(EIdentityCard.SIM.name())) {
            identityCard = EIdentityCard.SIM;
        } else if (request.getIdentityCard().equalsIgnoreCase(EIdentityCard.PASSPORT.name())) {
            identityCard = EIdentityCard.PASSPORT;
        } else {
            throw new IllegalStateException("Unexpected value: " + request.getIdentityCard());
        }
        return identityCard;
    }
}
