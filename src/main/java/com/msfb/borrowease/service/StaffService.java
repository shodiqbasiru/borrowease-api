package com.msfb.borrowease.service;

import com.msfb.borrowease.entity.Staff;
import com.msfb.borrowease.model.request.StaffRequest;
import com.msfb.borrowease.model.response.StaffResponse;

import java.util.List;

public interface StaffService {
    Staff createStaff(Staff staff);
    Staff getById(String id);
    StaffResponse getStaffById(String id);
    List<StaffResponse> getAllStaffs();
    StaffResponse updateStaff(StaffRequest request);
    void deleteStaff(String id);
}
