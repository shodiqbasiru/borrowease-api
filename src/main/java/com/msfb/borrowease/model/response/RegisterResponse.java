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
    private String username;
    private String role;
    private String phoneNumber;
    private String birthDate;
    private String createdAt;
    private String updatedAt;
}
