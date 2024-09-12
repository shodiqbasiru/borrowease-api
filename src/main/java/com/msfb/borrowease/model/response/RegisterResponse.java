package com.msfb.borrowease.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String status;
    private String username;
    private String role;
    private String phoneNumber;
    private String email;
    private String identityCard;
    private String jobName;
    private String npwp;
    private String birthDate;
    private String createdAt;
    private String updatedAt;
}
