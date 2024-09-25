package com.msfb.borrowease.service;

import com.msfb.borrowease.entity.Staff;

import java.util.List;

public interface StaffService {
    Staff createStaff(Staff staff);
    Staff getById(String id);
    List<Staff> getAllStaffs();
    Staff updateStaff(Staff staff);
    void deleteStaff(String id);
}
