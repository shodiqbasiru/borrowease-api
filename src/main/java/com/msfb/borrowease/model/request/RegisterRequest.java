package com.msfb.borrowease.model.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String motherName;
    private String status;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String birthDate;
    private String address;
    private String lastEducation;
    private String identityCard;
    private String identityNumber;
    private String jobName;
    private String companyName;
    private Double salary;
    private String npwp;
}
