package com.msfb.borrowease.model.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterStaffResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String role;
    private String phoneNumber;
    private String address;
    private String createdAt;
    private String updatedAt;
}
