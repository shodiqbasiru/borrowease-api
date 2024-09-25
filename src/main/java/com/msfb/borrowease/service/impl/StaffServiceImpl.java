package com.msfb.borrowease.service.impl;

import com.msfb.borrowease.entity.Staff;
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
    public List<Staff> getAllStaffs() {
        return repository.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Staff updateStaff(Staff staff) {
        Staff currentStaff = getById(staff.getId());

        currentStaff.setFirstName(staff.getFirstName());
        currentStaff.setLastName(staff.getLastName());
        currentStaff.setPhoneNumber(staff.getPhoneNumber());
        currentStaff.setEmail(staff.getEmail());
        currentStaff.setAddress(staff.getAddress());
        currentStaff.setUpdatedAt(new Date());
        return repository.saveAndFlush(currentStaff);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteStaff(String id) {
        Staff staff = getById(id);
        repository.delete(staff);
    }
}
