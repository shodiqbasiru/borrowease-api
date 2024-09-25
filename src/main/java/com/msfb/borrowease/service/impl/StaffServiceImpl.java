package com.msfb.borrowease.service.impl;

import com.msfb.borrowease.entity.Staff;
import com.msfb.borrowease.entity.User;
import com.msfb.borrowease.mapping.StaffMapping;
import com.msfb.borrowease.model.request.StaffRequest;
import com.msfb.borrowease.model.response.StaffResponse;
import com.msfb.borrowease.repository.StaffRepository;
import com.msfb.borrowease.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository repository;

    @Autowired
    public StaffServiceImpl(StaffRepository repository) {
        this.repository = repository;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Staff createStaff(Staff staff) {
        return repository.saveAndFlush(staff);
    }

    @Transactional(readOnly = true)
    @Override
    public Staff getById(String id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Staff not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public StaffResponse getStaffById(String id) {
        Staff staff = getById(id);
        return StaffMapping.toStaffResponse(staff);
    }

    @Transactional(readOnly = true)
    @Override
    public List<StaffResponse> getAllStaffs() {
        List<Staff> staff = repository.findAll();
        return StaffMapping.toStaffResponses(staff);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StaffResponse updateStaff(StaffRequest request) {
        Staff currentStaff = getById(request.getId());

        currentStaff.setFirstName(request.getFirstName());
        currentStaff.setLastName(request.getLastName());
        currentStaff.setPhoneNumber(request.getPhoneNumber());
        currentStaff.setEmail(request.getEmail());
        currentStaff.setAddress(request.getAddress());
        currentStaff.setUpdatedAt(new Date());

        Staff staff = repository.saveAndFlush(currentStaff);
        return StaffMapping.toStaffResponse(staff);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteStaff(String id) {
        Staff staff = getById(id);
        User user = staff.getUser();
        user.setDeletedAt(new Date());
        user.setIsEnable(false);
    }
}
