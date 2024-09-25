package com.msfb.borrowease.mapping;

import com.msfb.borrowease.entity.Staff;
import com.msfb.borrowease.model.response.StaffResponse;

import java.util.List;

public class StaffMapping {
    public static StaffResponse toStaffResponse(Staff staff) {
        return StaffResponse.builder()
                .id(staff.getId())
                .firstName(staff.getFirstName())
                .lastName(staff.getLastName())
                .email(staff.getEmail())
                .phoneNumber(staff.getPhoneNumber())
                .address(staff.getAddress())
                .build();
    }

    public static List<StaffResponse> toStaffResponses(List<Staff> staffs) {
        return staffs.stream().map(StaffMapping::toStaffResponse).toList();
    }
}
